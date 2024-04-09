package ru.stepa.alice.core

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AliceResponse(
    val version: String,
    val session: AliceSession,
    val response: Response
) {
    companion object {
        fun unknownCommand(aliceFullRequest: AliceFullRequest): AliceResponse {
            return AliceResponse(
                aliceFullRequest.version,
                aliceFullRequest.session,
                Response(
                    text = Constants.UNKNOWN_COMMAND,
                    endSession = false
                )
            )
        }
    }
}

@Serializable
data class Response(
    val text: String,
    val endSession: Boolean
)
