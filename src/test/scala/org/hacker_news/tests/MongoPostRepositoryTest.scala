package org.hacker_news.tests

import java.time.ZonedDateTime
import java.util.UUID

import org.hacker_news.data.concrete.mongo.MongoPostRepository
import org.hacker_news.domain.entities.Post
import org.mongodb.scala.MongoClient
import org.mongodb.scala.bson.BsonString
import org.mongodb.scala.model.Filters.{equal => mgEqual}
import org.scalatest.flatspec.AsyncFlatSpec
import org.scalatest.matchers.should.Matchers

class MongoPostRepositoryTest extends AsyncFlatSpec with Matchers {

  "MongoPostRepository" should "add post" in {
    val repository = new MongoPostRepository
    val post = Post(UUID.randomUUID().toString, "title", "This is a post text body", 0)
    repository.create(post).flatMap(_ =>
      MongoClient("mongodb://localhost:27013").getDatabase("posts")
        .getCollection("posts")
        .find(mgEqual("_id", post.id))
        .first().toFuture.map(res => {
        res.toMap.getOrElse("_id", BsonString("")).asString().getValue shouldEqual post.id
      }
    ))
  }
}
