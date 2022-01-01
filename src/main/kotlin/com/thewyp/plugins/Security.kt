package com.thewyp.plugins

import io.ktor.auth.*
import io.ktor.auth.jwt.*
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.application.*
import java.util.*

fun Application.configureSecurity() {

    authentication {
        jwt {
            val jwtAudience = environment.config.property("jwt.audience").getString()
            realm = environment.config.property("jwt.realm").getString()
            verifier(
                JWT
                    .require(Algorithm.HMAC256("secret"))
                    .withAudience(jwtAudience)
                    .withIssuer(environment.config.property("jwt.domain").getString())
                    .build()
            )
            validate { credential ->
                if (credential.payload.audience.contains(jwtAudience)) {
                    JWTPrincipal(credential.payload)
                } else null
            }
        }
    }
}

fun generateToken(
    email: String,
    jwtIssuer: String,
    jwtAudience: String,
    jwtSecret: String,
): String {
    val expiresIn = 1000L * 60L * 60L * 24L * 365L
    return JWT.create()
        .withClaim("email", email)
        .withIssuer(jwtIssuer)
        .withExpiresAt(Date(System.currentTimeMillis() + expiresIn))
        .withAudience(jwtAudience)
        .sign(Algorithm.HMAC256(jwtSecret))
}

val JWTPrincipal.email: String?
    get() = getClaim("email", String::class)
