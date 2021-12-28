package com.thewyp.data.requests

data class FollowUpdateRequest(
    val followingUserId: String,
    val followedUserId: String,
)