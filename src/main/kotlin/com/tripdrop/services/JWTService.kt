package com.tripdrop.services

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import java.util.*

fun generateToken(userId: String): String {
    val jwtIssuer = "com.example"
    val jwtAudience = "com.example.audience"
    val jwtRealm = "com.example.realm"
    val secret = "jwt-secret"

    return JWT.create()
        .withAudience(jwtAudience)
        .withIssuer(jwtIssuer)
        .withClaim("userId", userId)
        .withExpiresAt(Date(System.currentTimeMillis() + 600000))
        .sign(Algorithm.HMAC256(secret))
}
