package com.thewyp.data.repository.comment

import com.thewyp.data.models.Comment

interface CommentRepository {

    suspend fun createComment(comment: Comment)

    suspend fun deleteComment(commentId: String): Boolean

    suspend fun getCommentsForPost(postId: String): List<Comment>

    suspend fun deleteCommentsFromPost(postId: String): Boolean

    suspend fun getComment(commentId: String): Comment?
}