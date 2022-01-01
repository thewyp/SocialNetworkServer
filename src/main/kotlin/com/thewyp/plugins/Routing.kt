package com.thewyp.plugins

import com.thewyp.routes.*
import com.thewyp.service.*
import io.ktor.application.*
import io.ktor.routing.*
import org.koin.ktor.ext.inject

fun Application.configureRouting() {
    val userService: UserService by inject()
    val followService: FollowService by inject()
    val postService: PostService by inject()
    val likeService: LikeService by inject()
    val commentService: CommentService by inject()

    val jwtIssuer = environment.config.property("jwt.domain").getString()
    val jwtAudience = environment.config.property("jwt.audience").getString()
    val jwtSecret = environment.config.property("jwt.secret").getString()
    routing {
        // User routes
        createUser(userService)
        loginUser(
            userService = userService,
            jwtIssuer = jwtIssuer,
            jwtAudience = jwtAudience,
            jwtSecret = jwtSecret
        )

        // Following routes
        followUser(followService)
        unfollowUser(followService)

        // Post routes
        createPost(postService, userService)
        getPostsForFollows(postService, userService)
        deletePost(postService, userService, likeService)

        // Like routes
        likeParent(likeService, userService)
        unlikeParent(likeService, userService)

        // comment routes
        createComment(commentService, userService)
        getCommentsForPost(commentService)
        deleteComment(commentService, userService, likeService)
    }
}
