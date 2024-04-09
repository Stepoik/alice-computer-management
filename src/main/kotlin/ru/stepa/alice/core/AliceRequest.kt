package ru.stepa.alice.core

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Nlu(
    @SerialName("tokens")
    val tokens: List<String>,
    @SerialName("entities")
    val entities: List<String>
)

@Serializable
data class AliceRequest(
    @SerialName("command")
    val command: String,
    @SerialName("original_utterance")
    val originalUtterance: String,
    @SerialName("nlu")
    val nlu: Nlu
)

@Serializable
data class AliceFullRequest(
    @SerialName("session")
    val session: AliceSession,
    @SerialName("version")
    val version: String,
    @SerialName("request")
    val request: AliceRequest
)
