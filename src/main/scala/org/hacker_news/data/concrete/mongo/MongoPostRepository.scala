package org.hacker_news.data.concrete.mongo

import com.typesafe.scalalogging.LazyLogging
import org.hacker_news.data.contract.PostRepository
import org.hacker_news.domain.entities.Post
import org.mongodb.scala.bson.BsonObjectId
import org.mongodb.scala.bson.collection.immutable.Document
import org.mongodb.scala.model.{Filters, Updates}
import Updates._
import com.mongodb.client.model.{FindOneAndUpdateOptions, ReturnDocument}
import org.bson.codecs.DocumentCodec
import org.bson.codecs.configuration.CodecRegistries
import org.mongodb.scala.bson.codecs.ImmutableDocumentCodec
import org.mongodb.scala.{MongoClient, MongoCollection, MongoDatabase}
import org.mongodb.scala.bson.codecs.Macros._
import org.mongodb.scala.MongoClient.DEFAULT_CODEC_REGISTRY
import org.bson.codecs.configuration.CodecRegistries.{fromProviders, fromRegistries}
import org.hacker_news.data.concrete.mongo.model.{DataAccessCollection, DataAccessPost}

import scala.concurrent.{ExecutionContext, Future}

class MongoPostRepository(implicit ec: ExecutionContext) extends PostRepository with DataAccessCollection with LazyLogging {

  override def create(post: Post): Future[Post] = {
    val dap = post.toDataAccess
    postsCollection.insertOne(dap).toFuture().map(res => {
      if (!res.wasAcknowledged)
        logger.warn(s"insert of post: $post was not acknowledged")
      dap.toEntity
    })
  }

}
