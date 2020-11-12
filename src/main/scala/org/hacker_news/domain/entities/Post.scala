package org.hacker_news.domain.entities

import java.time.{ZoneId, ZonedDateTime}

case class Post(id: String, title: String, text: String, score: Int, createdAt: ZonedDateTime = ZonedDateTime.now(ZoneId.of("UTC")), votes: Int = 0)
