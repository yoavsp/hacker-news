package org.hacker_news

import akka.http.scaladsl.Http
import com.typesafe.scalalogging.LazyLogging
import org.hacker_news.data.concrete.mongo.{MongoPostRepository, MongoVotesRepository}
import org.hacker_news.domain.concrete.PostServiceImpl
import org.hacker_news.web.routing.PostRouting

import scala.concurrent.ExecutionContext

object Boot extends App with PostRouting with LazyLogging{
  override implicit def executionContext: ExecutionContext = system.dispatcher

  val postRepository = new MongoPostRepository
  val voteRepository = new MongoVotesRepository
  override val postService = new PostServiceImpl(postRepository, voteRepository)

  Http().bindAndHandle(handler = postRoutes, interface = serverHost , port = port) map { binding =>
    logger.info(s"REST interface bound to ${binding.localAddress}")
  } recover { case ex =>
    logger.info(s"REST interface could not bind to $serverHost:$port", ex.getMessage)
  }
}
