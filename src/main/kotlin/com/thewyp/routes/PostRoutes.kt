package com.thewyp.routes

import com.thewyp.data.requests.CreatePostRequest
import com.thewyp.data.requests.DeletePostRequest
import com.thewyp.data.responses.BasicApiResponse
import com.thewyp.service.LikeService
import com.thewyp.service.PostService
import com.thewyp.service.UserService
import com.thewyp.util.ApiResponseMessages
import com.thewyp.util.Constants
import com.thewyp.util.QueryParams
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.auth.jwt.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.createPost(
    postService: PostService,
    userService: UserService
) {
    authenticate {
        post("/api/post/create") {
            val request = call.receiveOrNull<CreatePostRequest>() ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
                return@post
            }

            ifEmailBelongsToUser(
                userId = request.userId,
                validateEmail = userService::doesEmailBelongToUserId
            ) {
                val didUserExist = postService.createPostIfUserExists(request)
                if (!didUserExist) {
                    call.respond(
                        HttpStatusCode.OK,
                        BasicApiResponse(
                            successful = false,
                            message = ApiResponseMessages.USER_NOT_FOUND
                        )
                    )
                } else {
                    call.respond(
                        HttpStatusCode.OK,
                        BasicApiResponse(
                            successful = true,
                        )
                    )
                }
            }
        }
    }
}

fun Route.getPostsForFollows(
    postService: PostService,
    userService: UserService
) {
    authenticate {
        get("/api/post/get") {
            val userId = call.parameters[QueryParams.PARAM_USER_ID] ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
                return@get
            }
            val page = call.parameters[QueryParams.PARAM_PAGE]?.toIntOrNull() ?: 0
            val pageSize =
                call.parameters[QueryParams.PARAM_PAGE_SIZE]?.toIntOrNull() ?: Constants.DEFAULT_POST_PAGE_SIZE

            ifEmailBelongsToUser(
                userId = userId,
                validateEmail = userService::doesEmailBelongToUserId
            ) {
                val posts = postService.getPostsByFollows(userId, page, pageSize)
                call.respond(
                    HttpStatusCode.OK,
                    posts)
            }
        }
    }
}

fun Route.deletePost(
    postService: PostService,
    userService: UserService,
    likeService: LikeService
) {
    delete("/api/post/delete") {
        val request = call.receiveOrNull<DeletePostRequest>() ?: kotlin.run {
            call.respond(HttpStatusCode.BadRequest)
            return@delete
        }
        val post = postService.getPost(request.postId)
        if(post == null) {
            call.respond(HttpStatusCode.NotFound)
            return@delete
        }
        ifEmailBelongsToUser(
            userId = post.userId,
            validateEmail = userService::doesEmailBelongToUserId
        ) {
            postService.deletePost(request.postId)
            likeService.deleteLikesForParent(request.postId)
            // TODO: Delete comments from post
            call.respond(HttpStatusCode.OK)
        }
    }
}