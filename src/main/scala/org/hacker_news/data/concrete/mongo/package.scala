package org.hacker_news.data.concrete

import java.time.{Instant, ZoneId, ZonedDateTime}

import com.typesafe.config.ConfigFactory
import com.typesafe.scalalogging.LazyLogging
import org.hacker_news.data.concrete.mongo.model.DataAccessPost
import org.hacker_news.data.contract.exceptions.PostNotFoundException
import org.hacker_news.domain.entities.Post
import org.mongodb.scala.SingleObservable
import org.mongodb.scala.bson.collection.immutable.Document

import scala.concurrent.{ExecutionContext, Future}

package object mongo {
  implicit class PostHelper(post: Post){
    def toDocument: Document = {
      Document("_id" -> post.id, "title" -> post.title, "text" -> post.text, "votes" -> post.votes, "createdAt" -> post.createdAt.toEpochSecond)
    }

    def toDataAccess: DataAccessPost = DataAccessPost(post.title, post.text, post.createdAt.toEpochSecond, post.votes)
  }

  implicit class DAPHelper(dap: DataAccessPost){
    def toEntity: Post = Post(Some(dap._id.toString),
      dap.title,
      dap.text,
      ZonedDateTime.ofInstant(Instant.ofEpochMilli(dap.createdAt), ZoneId.of("UTC")),
      dap.votes)
  }

  implicit class LoggingSingleObservable[T <: {def wasAcknowledged(): Boolean}](observable: SingleObservable[T])(implicit ec: ExecutionContext) extends LazyLogging {
    def toLoggedFuture(action: String = "unknown"): Future[T] = {
      observable.toFuture().map(res => {
        if (!res.wasAcknowledged)
          logger.warn(s"$action action was not acknowledged")
            res}).recover {
        case up: Throwable =>  {
          logger.error(s"$action action failed with exception: $up")
          throw up
        }
      }
    }
  }

  implicit class LoggingSingleObservableOf[T](observable: SingleObservable[T])(implicit ec: ExecutionContext) extends LazyLogging {
    def toLoggedFuture[B](id: String, action: String = "unknown"): Future[T] = {
      observable.toFuture().map(res => {
        if (res == null)
          throw PostNotFoundException(id)
        res}).recover {
        case up: Throwable =>  {
          logger.error(s"$action action failed with exception: $up")
          throw up
        }
      }
    }
  }


  object Configuration extends LazyLogging{
    lazy private val config = ConfigFactory.load()
    val mongoHost = config.getString("mongodb.host.url")
  }
}
