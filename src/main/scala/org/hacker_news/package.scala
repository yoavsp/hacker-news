package org

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer

package object hacker_news {
  implicit val system: ActorSystem = ActorSystem("hacker_news")
  implicit val materializer: ActorMaterializer = ActorMaterializer()

  val serverHost = "0.0.0.0"

  val port = 8090
}
