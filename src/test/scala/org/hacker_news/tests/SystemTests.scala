package org.hacker_news.tests

import org.hacker_news.data.concrete.mongo.model.DataAccessCollection
import org.hacker_news.web.serialization.PostJsonFormats
import org.scalatest.flatspec.AsyncFlatSpec
import org.scalatest.matchers.should.Matchers
import scala.concurrent.Future

class SystemTests extends AsyncFlatSpec with Matchers with PostJsonFormats with DataAccessCollection{

  "App" should "support post creation without data loss" in {
    val postCount = 10000
    (for{
      _ <- postDB.drop().toFuture()
      responses <- Future.sequence((1 to postCount).map(_ =>createPost))
      documentCount <- postsCollection.countDocuments().toFuture()
    } yield documentCount) map{count =>
      count shouldEqual postCount
    }
  }

  "App" should "consider upvotes in top votes" in {
    val postCount = 100

    (for{
      _ <- postDB.drop().toFuture()
      responses <- Future.sequence((1 to postCount).map(_ =>createPost))
      candidates = responses.take(100)
      _ <- Future.sequence(candidates.map(r => upvotePost(r.id)))
      topPosts <- getTopPosts
    } yield (candidates, topPosts)) collect {
      case (a,b) =>
        a.map(_.id).toList.sorted shouldEqual b.map(_.id).sorted
    }
  }
}

