package org.hacker_news.tests

import java.util.UUID

import akka.http.scaladsl.model.{ContentType, ContentTypes, HttpEntity}
import akka.http.scaladsl.testkit.ScalatestRouteTest
import org.hacker_news.data.contract.PostRepository
import org.hacker_news.domain.concrete.PostServiceImpl
import org.hacker_news.domain.contarct.PostService
import org.hacker_news.domain.entities.Post
import org.hacker_news.dto.{NewPostDTO, PostDTO}
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

  val mockRepository = mock(classOf[PostRepository])
  when(mockRepository.create(any[Post])) thenAnswer { invocation =>
    val post = invocation.getArgument[Post](0)
    Future.successful(post)
  }
  override val postService: PostService = new PostServiceImpl(mockRepository)

  "PostRouting" should {
    "create post" in {
      val newPost = NewPostDTO(UUID.randomUUID().toString, "body")
      Post("/api/v1/post", entity = HttpEntity(ContentTypes.`application/json`, newPost.toJson.prettyPrint)) ~>
        postRoutes ~> check(responseAs[PostDTO].title shouldEqual newPost.title)
    }


  }
}
