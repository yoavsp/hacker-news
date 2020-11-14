package org.hacker_news

import java.util.UUID

import org.hacker_news.domain.entities.Post

package object dto {
  implicit class NewPostDTOHelper(postDTO: NewPostDTO){
    def toEntity: Post =  Post(id = None, title= postDTO.title, text = postDTO.text)
  }

  implicit class PostHelper(post: Post){
    assert(post.id.isDefined)
    def toDTO: PostDTO = PostDTO(post.id.get, post.title, post.text, post.createdAt, post.votes)
  }
}
