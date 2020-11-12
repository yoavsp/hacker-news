package org.hacker_news.domain.contarct

import org.hacker_news.data.contract.PostRepository
import org.hacker_news.domain.entities.Post
import org.hacker_news.dto._

import scala.concurrent.{ExecutionContext, Future}

trait PostService {
  protected val postRepository: PostRepository
  implicit val ec: ExecutionContext

  def create(post: NewPostDTO): Future[PostDTO] = postRepository.create(post.toEntity).map(_.toDTO)


}