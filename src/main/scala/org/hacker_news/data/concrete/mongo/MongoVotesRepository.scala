package org.hacker_news.data.concrete.mongo

import com.mongodb.client.model.{FindOneAndUpdateOptions, ReturnDocument}
import com.typesafe.scalalogging.LazyLogging
import org.hacker_news.data.concrete.mongo.model.DataAccessCollection
import org.hacker_news.data.contract.VoteRepository
import org.mongodb.scala.bson.BsonObjectId
import org.mongodb.scala.model.{Filters, Updates}
import org.mongodb.scala.model.Updates._

import scala.concurrent.{ExecutionContext, Future}

class MongoVotesRepository(implicit ec: ExecutionContext) extends VoteRepository  with DataAccessCollection with LazyLogging {
  private val options = new FindOneAndUpdateOptions().returnDocument(ReturnDocument.AFTER)
  override def upvote(postId: String): Future[Int] = postsCollection
    .findOneAndUpdate(Filters.equal("_id", BsonObjectId(postId)),
      inc("votes", 1), options)
    .toLoggedFuture(postId, "upvote").map(res => res.votes)

  override def downvote(postId: String): Future[Int] = postsCollection
    .findOneAndUpdate(Filters.equal("_id", BsonObjectId(postId)),
      inc("votes", -1), options)
    .toLoggedFuture(postId, "upvote").map(res => res.votes)
}
