package com.thewyp.routes

import com.thewyp.data.requests.LikeUpdateRequest
import com.thewyp.data.responses.BasicApiResponse
import com.thewyp.service.LikeService
import com.thewyp.service.UserService
import com.thewyp.util.ApiResponseMessages
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.likeParent(
    likeService: LikeService,
    userService: UserService
) {
    authenticate {
        post("/api/like") {
            val request = call.receiveOrNull<LikeUpdateRequest>() ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
                return@post
            }
            ifEmailBelongsToUser(
                userId = request.userId,
                validateEmail = userService::doesEmailBelongToUserId
            ) {
                val likeSuccessful = likeService.likeParent(request.userId, request.parentId)
                if(likeSuccessful) {
                    call.respond(
                        HttpStatusCode.OK,
                        BasicApiResponse(
                            successful = true
                        )
                    )
                } else {
                    call.respond(
                        HttpStatusCode.OK,
                        BasicApiResponse(
                            successful = false,
                            message = ApiResponseMessages.USER_NOT_FOUND
                        )
                    )
                }
            }
        }
    }
}

fun Route.unlikeParent(
    likeService: LikeService,
    userService: UserService
) {
    authenticate {
        delete("/api/unlike") {
            val request = call.receiveOrNull<LikeUpdateRequest>() ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
                return@delete
            }
            ifEmailBelongsToUser(
                userId = request.userId,
                validateEmail = userService::doesEmailBelongToUserId
            ) {
                val unlikeSuccessful = likeService.unlikeParent(request.userId, request.parentId)
                if(unlikeSuccessful) {
                    call.respond(
                        HttpStatusCode.OK,
                        BasicApiResponse(
                            successful = true
                        )
                    )
                } else {
                    call.respond(
                        HttpStatusCode.OK,
                        BasicApiResponse(
                            successful = false,
                            message = ApiResponseMessages.USER_NOT_FOUND
                        )
                    )
                }
            }
        }
    }
}