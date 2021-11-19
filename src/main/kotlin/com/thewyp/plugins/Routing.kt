package com.thewyp.plugins

import com.thewyp.data.repository.user.UserRepository
import com.thewyp.routes.createUserRoute
import io.ktor.routing.*
import io.ktor.application.*
import org.koin.ktor.ext.inject

fun Application.configureRouting() {
    val userRepository: UserRepository by inject()
    routing {
        createUserRoute(userRepository)
    }
}
