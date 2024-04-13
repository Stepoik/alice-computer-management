package ru.stepa.alice.core

import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AliceUser(
    @SerialName("user_id")
    @EncodeDefault
    val userId: String? = null,
    @SerialName("access_token")
    @EncodeDefault
    val accessToken: String? = null
)

@Serializable
data class AliceApplication(
    @SerialName("application_id")
    val applicationId: String,
)

@Serializable
data class AliceSession(
    @SerialName("message_id")
    val messageId: Int,
    @SerialName("session_id")
    val sessionId: String,
    @SerialName("skill_id")
    val skillId: String,
    @SerialName("user_id")
    val userId: String?,
    @SerialName("user")
    val aliceUser: AliceUser?,
    @SerialName("application")
    val application: AliceApplication,
    @SerialName("new")
    val new: Boolean
)