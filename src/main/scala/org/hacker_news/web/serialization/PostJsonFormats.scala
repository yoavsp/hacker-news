package org.hacker_news.web.serialization

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import org.hacker_news.dto.{NewPostDTO, PostDTO, UpdatedPostDTO}
import spray.json.DefaultJsonProtocol
import spray.json._

trait PostJsonFormats extends SprayJsonSupport with DefaultJsonProtocol with JsonZonedDateTimeProtocol{
  implicit val NewPostFormat: RootJsonFormat[NewPostDTO] = jsonFormat2(NewPostDTO)
  implicit val UpdatedPostFormat: RootJsonFormat[UpdatedPostDTO] = jsonFormat2(UpdatedPostDTO)
  implicit val PostFormat: RootJsonFormat[PostDTO] = jsonFormat5(PostDTO)
}
