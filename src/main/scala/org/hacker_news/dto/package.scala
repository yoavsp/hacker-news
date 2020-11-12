package org.hacker_news

import java.util.UUID

import org.hacker_news.domain.entities.Post

package object dto {
  implicit class NewPostDTOHelper(postDTO: NewPostDTO){
    def toEntity: Post =  Post(id = UUID.randomUUID().toString, title= postDTO.title, text = postDTO.text, 0)
  }

  implicit class PostHelper(post: Post){
    def toDTO: PostDTO = PostDTO(post.id, post.title, post.text, post.createdAt, post.votes)
  }
}
