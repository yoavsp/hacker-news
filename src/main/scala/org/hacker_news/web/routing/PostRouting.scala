package org.hacker_news.web.routing

import akka.http.scaladsl.model.HttpResponse
import akka.http.scaladsl.server._
import akka.http.scaladsl.server.directives.MarshallingDirectives
import org.hacker_news.domain.contarct.PostService
import org.hacker_news.domain.entities.Post
import org.hacker_news.dto.{NewPostDTO, UpdatedPostDTO}
import org.hacker_news.web.serialization.PostJsonFormats

import scala.concurrent.ExecutionContext


trait PostRouting extends Directives with MarshallingDirectives with PostJsonFormats {

  implicit def executionContext: ExecutionContext

  val postService: PostService

  def postRoutes: Route =

    pathPrefix("api" / "v1" / "post") {
        post {
          entity(as[NewPostDTO]) { post =>
            complete(postService.create(post))
          }

      } ~ handleExceptions(hackerNewsExceptionHandler) {
          pathPrefix(PathMatchers.Segment) { postId =>
            pathEnd {
              patch {
                entity(as[UpdatedPostDTO]) { post =>
                  complete(postService.update(postId, post))
                }
              }
            } ~ path("upvote") {
              post {
                complete(postService.upvote(postId).map(_ => HttpResponse()))
              }
            }~ path("downvote") {
              post {
                complete(postService.downvote(postId).map(_ => HttpResponse()))
              }
            }
          }
        } ~ get {
          complete(postService.getTopVotes(100))
        }

    }

}


