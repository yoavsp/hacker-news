package org.hacker_news.data.contract

import scala.concurrent.Future

trait VoteRepository {
  def upvote(postId: String): Future[Int]

  def downvote(postId: String): Future[Int]
}
