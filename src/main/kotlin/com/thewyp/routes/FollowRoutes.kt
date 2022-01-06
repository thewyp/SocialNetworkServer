package com.thewyp.routes

import com.thewyp.data.repository.follow.FollowRepository
import com.thewyp.data.requests.FollowUpdateRequest
import com.thewyp.data.responses.BasicApiResponse
import com.thewyp.plugins.userId
import com.thewyp.service.FollowService
import com.thewyp.util.Constants.USER_NOT_FOUND
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.followUser(followService: FollowService) {
    authenticate {
        post("/api/following/follow") {
            val request = call.receiveOrNull<FollowUpdateRequest>() ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
                return@post
            }
            println("followUser: userId=${call.userId}")
            val didUserExist = followService.followUserIfExists(request, call.userId)
            if(didUserExist) {
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
                        message = USER_NOT_FOUND
                    )
                )
            }
        }
    }
}

fun Route.unfollowUser(followService: FollowService) {
    authenticate {
        delete("/api/following/unfollow") {
            val request = call.receiveOrNull<FollowUpdateRequest>() ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
                return@delete
            }
            val didUserExist = followService.unfollowUserIfExists(request, call.userId)
            if(didUserExist) {
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
                        message = USER_NOT_FOUND
                    )
                )
            }
        }
    }

}