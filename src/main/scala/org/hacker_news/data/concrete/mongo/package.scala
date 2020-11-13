package org.hacker_news.data.concrete

import java.time.{Instant, ZoneId, ZoneOffset, ZonedDateTime}

import org.bson.{BsonReader, BsonWriter}
import org.bson.codecs.{Codec, DecoderContext, EncoderContext}
import org.hacker_news.data.concrete.mongo.model.DataAccessPost
import org.hacker_news.domain.entities.Post
import org.mongodb.scala.bson.{BsonInt32, BsonInt64, BsonObjectId, BsonString}
import org.mongodb.scala.bson.collection.immutable.Document
import org.mongodb.scala.bson.conversions.Bson
import org.mongodb.scala.model.Filters
import org.mongodb.scala.model.Updates.{combine, set}

package object mongo {
  implicit class PostHelper(post: Post){
    def toDocument: Document = {
      Document("_id" -> post.id, "title" -> post.title, "text" -> post.text, "votes" -> post.votes, "createdAt" -> post.createdAt.toEpochSecond, "score" -> post.score)
    }

    def toDataAccess: DataAccessPost = DataAccessPost(post.title, post.text, (post.createdAt.toEpochSecond / 1440).toInt, post.createdAt.toEpochSecond, post.votes)
  }

  implicit class DAPHelper(dap: DataAccessPost){
    def toEntity: Post = Post(Some(dap._id.toString),
      dap.title,
      dap.text,
      dap.score,
      ZonedDateTime.ofInstant(Instant.ofEpochMilli(dap.createdAt), ZoneId.of("UTC")),
      dap.votes)
  }

  class SomeCodec extends Codec[Some[String]] {
    override def decode(reader: BsonReader, decoderContext: DecoderContext): Some[String] = Some(reader.readString())

    override def encode(writer: BsonWriter, value: Some[String], encoderContext: EncoderContext): Unit = value match{
      case Some(v) => writer.writeString(v)
      case _ =>
    }

    override def getEncoderClass: Class[Some[String]] = classOf[Some[String]]
  }
}
