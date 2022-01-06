package com.thewyp.service

import com.thewyp.data.repository.follow.FollowRepository
import com.thewyp.data.requests.FollowUpdateRequest

class FollowService(
    private val followRepository: FollowRepository
) {

    suspend fun followUserIfExists(request: FollowUpdateRequest, followingUserId: String): Boolean {
        return followRepository.followUserIfExists(
            followingUserId,
            request.followedUserId
        )
    }

    suspend fun unfollowUserIfExists(request: FollowUpdateRequest, followingUserId: String): Boolean {
        return followRepository.unfollowUserIfExists(
            followingUserId,
            request.followedUserId
        )
    }
}