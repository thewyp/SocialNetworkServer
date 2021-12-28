package com.thewyp.plugins

import com.thewyp.data.repository.follow.FollowRepository
import com.thewyp.data.repository.user.UserRepository
import com.thewyp.routes.createUserRoute
import com.thewyp.routes.followUser
import com.thewyp.routes.loginUser
import com.thewyp.routes.unfollowUser
import io.ktor.routing.*
import io.ktor.application.*
import org.koin.ktor.ext.inject

fun Application.configureRouting() {
    val userRepository: UserRepository by inject()
    val followRepository: FollowRepository by inject()
    routing {
        // user
        createUserRoute(userRepository)
        loginUser(userRepository)

        // follow
        followUser(followRepository)
        unfollowUser(followRepository)
    }
}
