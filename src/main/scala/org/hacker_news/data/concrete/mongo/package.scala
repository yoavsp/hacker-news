package org.hacker_news.data.concrete

import java.time.ZoneOffset

import org.hacker_news.domain.entities.Post
import org.mongodb.scala.bson.collection.immutable.Document

package object mongo {
  implicit class PostHelper(post: Post){
    def toDocument: Document = {
      Document("_id" -> post.id, "title" -> post.title, "text" -> post.text, "votes" -> post.votes, "createdAt" -> post.createdAt.toEpochSecond / 1440, "score" -> post.score)
    }
  }

  case class UnacknowledgedPostException(post: Post) extends Exception(s"post: $post was not acknowledged")
}
