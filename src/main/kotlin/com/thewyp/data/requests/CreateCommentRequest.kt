package com.thewyp.data.requests

data class CreateCommentRequest(
    val comment: String,
    val postId: String,
    val userId: String
)