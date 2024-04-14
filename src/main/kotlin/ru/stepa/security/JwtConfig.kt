package ru.stepa.security

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.http.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*
import org.koin.mp.KoinPlatform

class JwtConfig {
    companion object Constants {
        // jwt config
        private const val jwtIssuer = "ru.stepa"
        private const val jwtRealm = "ru.stepa"
        private val jwtAudience = "ru.stepa"
        private val jwtSecret = "myjestkiysecret"

        // claims
        private const val CLAIM_USERID = "userId"
        private const val CLAIM_USERNAME = "userName"

        // strings
        private const val TOKEN_NOT_AVAILABLE = "Token not available"
    }

    private val jwtAlgorithm = Algorithm.HMAC512(jwtSecret)
    private val jwtVerifier: JWTVerifier = JWT
        .require(jwtAlgorithm)
        .withIssuer(jwtIssuer)
        .build()

    /**
     * Generate a token for a authenticated user
     */
    fun generateToken(user: JwtUser): String = JWT.create()
        .withSubject("Authentication")
        .withIssuer(jwtIssuer)
        .withAudience(jwtAudience)
        .withClaim(CLAIM_USERID, user.userId)
        .withClaim(CLAIM_USERNAME, user.userName)
        .sign(jwtAlgorithm)

    /**
     * Configure the jwt ktor authentication feature
     */
    fun configureKtorFeature(config: JWTAuthenticationProvider.Config) = with(config) {
        verifier(jwtVerifier)
        realm = jwtRealm
        validate { credential ->
            val userId = credential.payload.getClaim(CLAIM_USERID).asString()
            val userName = credential.payload.getClaim(CLAIM_USERNAME).asString()
            if (credential.payload.audience.contains(jwtAudience))
                JwtUser(
                    userId = userId,
                    userName = userName
                )
            else null
        }
        challenge { _, _ ->
            call.respond(HttpStatusCode.Unauthorized, TOKEN_NOT_AVAILABLE)
        }
    }
}