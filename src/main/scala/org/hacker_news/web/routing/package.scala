package org.hacker_news.web

import akka.http.scaladsl.model.HttpResponse
import akka.http.scaladsl.model.StatusCodes.NotFound
import akka.http.scaladsl.server.Directives.{complete, extractUri}
import akka.http.scaladsl.server.ExceptionHandler
import org.hacker_news.data.contract.exceptions.PostNotFoundException

package object routing {
  val hackerNewsExceptionHandler = ExceptionHandler {
    case ex: PostNotFoundException =>
      extractUri { uri =>
        complete(HttpResponse(NotFound))
      }
  }
}
