package org.hacker_news.web.serialization

import java.time.{Instant, ZoneId, ZonedDateTime}
import java.time.format.DateTimeFormatter

import spray.json.{DefaultJsonProtocol, JsString, JsValue, RootJsonFormat, deserializationError}

import scala.util.Try

trait JsonZonedDateTimeProtocol extends DefaultJsonProtocol  {

  implicit object ZonedDateTimeFormat extends RootJsonFormat[ZonedDateTime] {

    val formatter: DateTimeFormatter = DateTimeFormatter.ISO_DATE_TIME.withZone(ZoneId.of("UTC"))

    def write(obj: ZonedDateTime): JsValue = {
      JsString(obj.format(formatter))
    }

    def read(json: JsValue): ZonedDateTime = json match {
      case JsString(s) => Try(ZonedDateTime.parse(s, formatter)).getOrElse(error(s))
      case _ => error(json.toString())
    }


    def error(v: Any): ZonedDateTime = {
      deserializationError(
        "invalid date format"
      )
    }
  }

  implicit class ZonedDateTimeHelper(timestamp: Long){
    def toZonedDateTime: ZonedDateTime = ZonedDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneId.of("UTC"))
  }
}
