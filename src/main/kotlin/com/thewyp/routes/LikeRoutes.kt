package com.thewyp.routes

import com.thewyp.data.requests.LikeUpdateRequest
import com.thewyp.data.responses.BasicApiResponse
import com.thewyp.plugins.userId
import com.thewyp.service.LikeService
import com.thewyp.service.UserService
import com.thewyp.util.ApiResponseMessages
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.auth.jwt.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.likeParent(
    likeService: LikeService
) {
    authenticate {
        post("/api/like/like") {
            val request = call.receiveOrNull<LikeUpdateRequest>() ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
                return@post
            }
            println("likeParent:userId=${call.userId}")
            val likeSuccessful = likeService.likeParent(call.userId, request.parentId)
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

fun Route.unlikeParent(
    likeService: LikeService
) {
    authenticate {
        delete("/api/like/unlike") {
            val request = call.receiveOrNull<LikeUpdateRequest>() ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
                return@delete
            }
            println("unlikeParent:userId=${call.userId}")
            val unlikeSuccessful = likeService.unlikeParent(call.userId, request.parentId)
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