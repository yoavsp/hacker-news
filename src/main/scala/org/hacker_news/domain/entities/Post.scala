package org.hacker_news.domain.entities

import java.time.{ZoneId, ZonedDateTime}

case class Post(id: Option[String], title: String, text: String, createdAt: ZonedDateTime = ZonedDateTime.now(ZoneId.of("UTC")), votes: Int = 0)
