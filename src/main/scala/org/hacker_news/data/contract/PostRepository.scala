package org.hacker_news.data.contract

import org.hacker_news.domain.entities.Post

import scala.concurrent.Future

trait PostRepository {
  def create(post: Post): Future[Post]
}
