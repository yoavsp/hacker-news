package org.hacker_news.data.concrete.mongo
package model

import org.bson.codecs.configuration.CodecRegistries.{fromProviders, fromRegistries}
import org.mongodb.scala.MongoClient.DEFAULT_CODEC_REGISTRY
import org.mongodb.scala.{MongoClient, MongoCollection, MongoDatabase}
import org.bson.codecs.configuration.CodecRegistries
import org.mongodb.scala.bson.codecs.ImmutableDocumentCodec
import org.mongodb.scala.{MongoClient, MongoCollection, MongoDatabase}
import org.mongodb.scala.bson.codecs.Macros._
import org.mongodb.scala.MongoClient.DEFAULT_CODEC_REGISTRY
import org.bson.codecs.configuration.CodecRegistries.{fromProviders, fromRegistries}
import org.hacker_news.data.concrete.mongo.model.DataAccessPost


trait DataAccessCollection {
  protected val mongoClient = MongoClient(Configuration.mongoHost)
  protected val postDB: MongoDatabase = mongoClient.getDatabase("posts")
  val codecRegistry = fromRegistries(fromProviders(classOf[DataAccessPost]), DEFAULT_CODEC_REGISTRY)
  protected val postsCollection: MongoCollection[DataAccessPost] = postDB.getCollection[DataAccessPost]("posts").withCodecRegistry(codecRegistry)
}
