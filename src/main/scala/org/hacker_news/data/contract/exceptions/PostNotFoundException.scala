package org.hacker_news.data.contract.exceptions

case class PostNotFoundException(id: String) extends Exception(s"post with id: $id was not found")
