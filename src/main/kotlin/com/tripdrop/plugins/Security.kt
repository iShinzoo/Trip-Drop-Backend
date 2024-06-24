package com.tripdrop.plugins

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*

fun Application.configureSecurity() {
    install(Authentication) {
        jwt {
            verifier(
                JWT
                    .require(Algorithm.HMAC256("jwt-secret"))
                    .withAudience("com.example.audience")
                    .withIssuer("com.example")
                    .build()
            )
            validate { credential ->
                if (credential.payload.getClaim("userId").asString() != "") JWTPrincipal(credential.payload) else null
            }
        }
    }
}
