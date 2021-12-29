package com.thewyp.service

import com.thewyp.data.models.Post
import com.thewyp.data.repository.post.PostRepository
import com.thewyp.data.requests.CreatePostRequest

class PostService(
    private val repository: PostRepository
) {

    suspend fun createPostIfUserExists(request: CreatePostRequest): Boolean {
        return repository.createPostIfUserExists(
            Post(
                imageUrl = "",
                userId = request.userId,
                timestamp = System.currentTimeMillis(),
                description = request.description
            )
        )
    }
}