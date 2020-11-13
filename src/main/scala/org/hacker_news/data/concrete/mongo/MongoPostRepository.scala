package org.hacker_news.data.concrete.mongo

import com.typesafe.scalalogging.LazyLogging
import org.hacker_news.data.concrete.mongo.model.DataAccessCollection
import org.hacker_news.data.contract.PostRepository
import org.hacker_news.domain.entities.Post

import scala.concurrent.{ExecutionContext, Future}

class MongoPostRepository(implicit ec: ExecutionContext) extends PostRepository with DataAccessCollection with LazyLogging {

  override def create(post: Post): Future[Post] = {
    val dap = post.toDataAccess
    postsCollection.insertOne(dap).toLoggedFuture("createPost").map(res => dap.toEntity)
  }

}
