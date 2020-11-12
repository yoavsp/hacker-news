package org.hacker_news.data.concrete.mongo

import com.mongodb.reactivestreams.client.MongoClients
import org.hacker_news.data.contract.PostRepository
import org.hacker_news.domain.entities.Post
import org.mongodb.scala.bson.collection.immutable.Document
import org.mongodb.scala.{MongoCollection, MongoDatabase, MongoClient}

import scala.concurrent.{ExecutionContext, Future}

class MongoPostRepository(implicit ec: ExecutionContext) extends PostRepository{
  private val mongoClient = MongoClient("mongodb://localhost:27013")
  private val database: MongoDatabase = mongoClient.getDatabase("posts")
  private val postsCollection: MongoCollection[Document] = database.getCollection("posts")
  override def create(post: Post): Future[Post] = postsCollection.insertOne(post.toDocument).toFuture flatMap{ res=>
    if(res.wasAcknowledged) Future.successful(post) else Future.failed(UnacknowledgedPostException(post))
  }

}
