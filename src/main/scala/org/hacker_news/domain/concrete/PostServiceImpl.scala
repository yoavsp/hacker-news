package org.hacker_news.domain.concrete

import org.hacker_news.data.contract.{PostRepository, VoteRepository}
import org.hacker_news.domain.contarct.PostService
import org.hacker_news.domain.entities.Post

import scala.concurrent.{ExecutionContext, Future}

class PostServiceImpl(val postRepository: PostRepository, val voteRepository: VoteRepository)(implicit val ec: ExecutionContext) extends PostService
