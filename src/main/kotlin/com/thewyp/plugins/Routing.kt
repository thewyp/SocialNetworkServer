package com.thewyp.plugins

import com.thewyp.data.repository.user.UserRepository
import com.thewyp.routes.*
import com.thewyp.service.FollowService
import com.thewyp.service.PostService
import com.thewyp.service.UserService
import io.ktor.application.*
import io.ktor.routing.*
import org.koin.ktor.ext.inject

fun Application.configureRouting() {
    val userRepository: UserRepository by inject()
    val userService: UserService by inject()
    val followService: FollowService by inject()
    val postService: PostService by inject()
    routing {
        // User routes
        createUserRoute(userService)
        loginUser(userRepository)

        // Following routes
        followUser(followService)
        unfollowUser(followService)

        // Post routes
        createPostRoute(postService)
    }
}
