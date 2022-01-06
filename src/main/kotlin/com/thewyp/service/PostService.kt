package com.thewyp.service

import com.thewyp.data.models.Post
import com.thewyp.data.repository.post.PostRepository
import com.thewyp.data.requests.CreatePostRequest

class PostService(
    private val repository: PostRepository
) {

    suspend fun createPostIfUserExists(request: CreatePostRequest, userId: String): Boolean {
        return repository.createPostIfUserExists(
            Post(
                imageUrl = "",
                userId = userId,
                timestamp = System.currentTimeMillis(),
                description = request.description
            )
        )
    }

    suspend fun getPostsByFollows(
        userId: String,
        page: Int,
        pageSize: Int
    ): List<Post> {
        return repository.getPostsByFollows(userId, page, pageSize)
    }

    suspend fun getPost(postId: String): Post? = repository.getPost(postId)

    suspend fun deletePost(postId: String) {
        repository.deletePost(postId)
    }
}