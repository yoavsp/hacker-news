package org.hacker_news.tests

import java.time.ZonedDateTime
import java.util.UUID

import akka.http.scaladsl.model.{ContentType, ContentTypes, HttpEntity}
import akka.http.scaladsl.testkit.ScalatestRouteTest
import org.hacker_news.data.contract.{PostRepository, VoteRepository}
import org.hacker_news.domain.concrete.PostServiceImpl
import org.hacker_news.domain.contarct.PostService
import org.hacker_news.domain.entities.{Post => HNPost}
import org.hacker_news.dto.{NewPostDTO, PostDTO, UpdatedPostDTO}
import org.hacker_news.web.routing.PostRouting
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.testkit.ScalatestRouteTest
import akka.http.scaladsl.server._
import Directives._
import org.hacker_news.web.serialization.PostJsonFormats
import spray.json._

import scala.concurrent.{ExecutionContext, ExecutionContextExecutor, Future}
import org.mockito.Mockito._
import org.mockito.ArgumentMatchers._

class PostRoutingTests
  extends AnyWordSpec
    with Matchers
    with ScalatestRouteTest
    with PostRouting
    with PostJsonFormats{


  override implicit def executor: ExecutionContextExecutor = this.createActorSystem().dispatcher

  val smallRoute: Route =
    get {
      concat(
        pathSingleSlash {
          complete {
            "Captain on the bridge!"
          }
        },
        path("ping") {
          complete("PONG!")
        }
      )
    }

  override implicit def executionContext: ExecutionContext = this.executionContext

  val mockPostRepository = mock(classOf[PostRepository])
  val mockVoteRepository = mock(classOf[VoteRepository])
  override val postService: PostService = new PostServiceImpl(mockPostRepository, mockVoteRepository)

  "PostRouting" should {
    "create post" in {
      when(mockPostRepository.create(any[HNPost])) thenAnswer { invocation =>
        val post = invocation.getArgument[HNPost](0)
        Future.successful(post.copy(id=Some("id")))
      }
      val newPost = NewPostDTO(UUID.randomUUID().toString, "body")
      Post("/api/v1/post", entity = HttpEntity(ContentTypes.`application/json`, newPost.toJson.prettyPrint)) ~>
        postRoutes ~> check(responseAs[PostDTO].title shouldEqual newPost.title)
    }

    "update post" in {
      val id = UUID.randomUUID().toString
      val updatedPost = UpdatedPostDTO(Some("new title"), Some("new body"))
      val post = HNPost(Some(id), updatedPost.title.get, updatedPost.text.get, 0, ZonedDateTime.now, 0)

      when(mockPostRepository.update(anyString, any[Option[String]], any[Option[String]])) thenAnswer { invocation =>
        Future.successful(post)
      }
      Patch(s"/api/v1/post/$id", entity = HttpEntity(ContentTypes.`application/json`, updatedPost.toJson.prettyPrint)) ~>
        postRoutes ~> check{
        responseAs[PostDTO].title shouldEqual updatedPost.title.get
        responseAs[PostDTO].text shouldEqual updatedPost.text.get
      }
    }

    "partially update post" in {
      val id = UUID.randomUUID().toString
      val updatedPost = UpdatedPostDTO(Some("new title"), None)
      val post = HNPost(Some(id), updatedPost.title.get, "some old text", 0, ZonedDateTime.now, 0)

      when(mockPostRepository.update(anyString, any[Option[String]], any[Option[String]])) thenAnswer { invocation =>
        Future.successful(post)
      }
      Patch(s"/api/v1/post/$id", entity = HttpEntity(ContentTypes.`application/json`, updatedPost.toJson.prettyPrint)) ~>
        postRoutes ~> check{
        responseAs[PostDTO].title shouldEqual updatedPost.title.get

      }
    }


  }
}
