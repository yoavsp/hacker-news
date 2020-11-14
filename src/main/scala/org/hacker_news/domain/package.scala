package org.hacker_news

import com.typesafe.config.ConfigFactory

package object domain {

  object Configuration{
    lazy private val config = ConfigFactory.load()
    val voteWeight: Float = config.getDouble("domain.weights.vote").toFloat
    val creationTimeWeight: Float = config.getDouble("domain.weights.creation-time").toFloat / 10000000000F
  }
}
