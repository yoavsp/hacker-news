package org.hacker_news.dto

import java.time.ZonedDateTime

case class PostDTO(id: String, title: String, text: String, createdAt: ZonedDateTime, votes: Int)
