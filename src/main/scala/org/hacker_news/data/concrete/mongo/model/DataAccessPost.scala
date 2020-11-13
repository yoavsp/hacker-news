package org.hacker_news.data.concrete.mongo.model

import org.mongodb.scala.bson.ObjectId

object DataAccessPost{
  def apply(title: String, text: String, score: Int, createdAt: Long, votes: Int): DataAccessPost =
    DataAccessPost(new ObjectId(), title, text, score, createdAt, votes)
}

case class DataAccessPost(_id: ObjectId, title: String, text: String, score: Int, createdAt: Long, votes: Int)
