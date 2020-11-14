package org.hacker_news.data.concrete.mongo

import com.mongodb.{BasicDBList, BasicDBObject, BasicDBObjectBuilder}
import com.mongodb.client.model.{FindOneAndUpdateOptions, ReturnDocument}
import com.typesafe.scalalogging.LazyLogging
import org.hacker_news.data.concrete.mongo.model.{DataAccessCollection, DataAccessPost}
import org.hacker_news.data.contract.PostRepository
import org.hacker_news.domain.entities.Post
import org.mongodb.scala.bson.BsonObjectId
import org.mongodb.scala.model.Updates._
import org.mongodb.scala.model.{Aggregates, Filters, Projections, Sorts}

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
      .toLoggedFuture(id, "updatePost").map { res =>
      res.toEntity
    }
  }

  override def getTopPosts(timeWeight: Float, votesWeight: Float, count: Int): Future[Seq[Post]] = {

    val transformationProjection = new BasicDBObject("$project",
      new BasicDBObject("text", "$text")
        .append("title", "$title")
        .append("votes", "$votes")
        .append("createdAt", "$createdAt")
        .append("normalizedTime", new BasicDBObject("$multiply", List("$createdAt", timeWeight)))
        .append("normalizedVotes", new BasicDBObject("$multiply", List("$votes", votesWeight))))

    val calculationProjection = new BasicDBObject("$project",
      new BasicDBObject("text", "$text")
        .append("title", "$title")
        .append("votes", "$votes")
        .append("createdAt", "$createdAt")
        .append("score", new BasicDBObject("$add", List("$votes", "$normalizedTime"))))

    postsCollection.aggregate(Seq(transformationProjection,
      calculationProjection,
      Aggregates.sort(Sorts.descending("score")),
      Aggregates.limit(count))).toLoggedFuture("getTopPosts").map(res => res.map(_.toEntity))

  }
}
