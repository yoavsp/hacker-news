package org.hacker_news.data.concrete.mongo

import com.mongodb.client.model.{FindOneAndUpdateOptions, ReturnDocument}
import com.typesafe.scalalogging.LazyLogging
import org.hacker_news.data.concrete.mongo.model.DataAccessCollection
import org.hacker_news.data.contract.PostRepository
import org.hacker_news.domain.entities.Post
import org.mongodb.scala.bson.BsonObjectId
import org.mongodb.scala.model.Updates._
import org.mongodb.scala.model.{Filters, Updates}

import scala.concurrent.{ExecutionContext, Future}

class MongoPostRepository(implicit ec: ExecutionContext) extends PostRepository with DataAccessCollection with LazyLogging {

  override def create(post: Post): Future[Post] = {
    val dap = post.toDataAccess
    postsCollection.insertOne(dap).toLoggedFuture("createPost").map(res => dap.toEntity)
  }


  override def update(id: String, titleOption: Option[String], textOption: Option[String]): Future[Post] = {
    val options = new FindOneAndUpdateOptions().returnDocument(ReturnDocument.AFTER)
    val update = (titleOption, textOption) match {
      case (Some(title), Some(text)) => combine(set("text", text), set("title", title))
      case (Some(title), None) => set("title", title)
      case (None, Some(text)) => set("text", text)
    }
    postsCollection
      .findOneAndUpdate(Filters.equal("_id", BsonObjectId(id)), update, options)
      .toLoggedFuture(id, "updatePost").map {res =>
      res.toEntity
    }
  }
}
