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
import org.hacker_news.data.concrete.mongo.model.DataAccessPost

import scala.concurrent.{ExecutionContext, Future}

class MongoPostRepository(implicit ec: ExecutionContext) extends PostRepository with LazyLogging {
  private val mongoClient = MongoClient("mongodb://localhost:27012")
  private val database: MongoDatabase = mongoClient.getDatabase("posts")
  val codecRegistry = fromRegistries(fromProviders(classOf[DataAccessPost]), DEFAULT_CODEC_REGISTRY, CodecRegistries.fromCodecs(new SomeCodec))
  private val postsCollection: MongoCollection[DataAccessPost] = database.getCollection[DataAccessPost]("posts").withCodecRegistry(codecRegistry)

  override def create(post: Post): Future[Post] = {
    val dap = post.toDataAccess
    postsCollection.insertOne(dap).toFuture().map(res => {
      if (!res.wasAcknowledged)
        logger.warn(s"insert of post: $post was not acknowledged")
      dap.toEntity
    })
  }


  override def update(id: String, title: Option[String], text: Option[String]): Future[Post] = {
    val options = new FindOneAndUpdateOptions().returnDocument(ReturnDocument.AFTER)

    postsCollection
      .findOneAndUpdate(Filters.equal("_id", BsonObjectId(id)), combine(set("text", text), set("title", title)), options).toFuture.map { res =>
        logger.warn(s"update post: ${res} ")
        res.toEntity
      }
  }
}
