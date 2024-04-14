package ru.stepa.security

import io.ktor.server.auth.*

data class JwtUser(
    val userId: String,
    val userName: String
): Principal
