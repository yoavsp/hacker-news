package org.hacker_news.domain.concrete

import org.hacker_news.data.contract.PostRepository
import org.hacker_news.domain.contarct.PostService
import org.hacker_news.domain.entities.Post

import scala.concurrent.{ExecutionContext, Future}

class PostServiceImpl(val postRepository: PostRepository)(implicit val ec: ExecutionContext) extends PostService
