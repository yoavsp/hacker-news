package org.hacker_news

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{ContentTypes, HttpEntity, HttpMethods, HttpRequest, HttpResponse, StatusCodes}
import akka.http.scaladsl.unmarshalling.Unmarshal
import akka.stream.ActorMaterializer
import com.github.javafaker.Faker
import org.hacker_news.data.concrete.mongo.model.DataAccessCollection
import org.hacker_news.domain.entities.Post
import org.hacker_news.dto.{NewPostDTO, PostDTO}
import org.hacker_news.web.serialization.PostJsonFormats
import org.mongodb.scala.model.Filters
import org.scalatest.flatspec.AsyncFlatSpec
import org.scalatest.matchers.should.Matchers
import spray.json._

import scala.concurrent.Future


import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.http.scaladsl.unmarshalling.Unmarshal
import com.github.javafaker.Faker
import org.hacker_news.dto.{NewPostDTO, PostDTO}
import org.hacker_news.web.serialization.PostJsonFormats
import spray.json._

import scala.concurrent.Future


package object tests extends PostJsonFormats{
  implicit val system = ActorSystem("SystemTests")

  import system.dispatcher
  val faker = new Faker()

  def createPost: Future[PostDTO] = Http().singleRequest(HttpRequest(HttpMethods.POST,
    uri = s"http://localhost:8090/api/v1/post/",
    entity = HttpEntity(ContentTypes.`application/json`,
      NewPostDTO(faker.chuckNorris().fact(), faker.lorem().paragraph(200)).toJson.prettyPrint))).flatMap(r =>Unmarshal(r.entity).to[PostDTO])

  def getTopPosts: Future[List[PostDTO]] = Http().singleRequest(HttpRequest(HttpMethods.GET,
    uri = s"http://localhost:8090/api/v1/post/")).flatMap(r =>Unmarshal(r.entity).to[List[PostDTO]])


  def upvotePost(id: String): Future[HttpResponse] = Http().singleRequest(HttpRequest(HttpMethods.POST,
    uri = s"http://localhost:8090/api/v1/post/$id/upvote"))
}
