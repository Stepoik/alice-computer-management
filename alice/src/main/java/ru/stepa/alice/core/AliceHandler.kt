package ru.stepa.alice.core

typealias Handler = suspend (AliceFullRequest) -> Response

@DslMarker
annotation class AliceDsl

object AliceHandler {
    private val registeredCommands = mutableMapOf<String, Handler>()
    private var unknownHandler: Handler? = null

    internal fun createCommand(vararg commands: String, body: Handler) {
        for (i in commands) {
            registeredCommands[i] = body
        }
    }

    internal fun registerUnknownCommand(body: Handler) {
        unknownHandler = body
    }

    suspend fun handle(aliceFullRequest: AliceFullRequest): AliceResponse {
        val command = aliceFullRequest.request.command
        val handler = registeredCommands[command] ?:
            return unknownHandler?.let { defaultHandler ->
                AliceResponse(
                    version = aliceFullRequest.version,
                    session = aliceFullRequest.session,
                    response = defaultHandler(aliceFullRequest)
                )
            } ?: AliceResponse.unknownCommand(aliceFullRequest)
        val response = handler(aliceFullRequest)
        return AliceResponse(
            version = aliceFullRequest.version,
            session = aliceFullRequest.session,
            response = response
        )
    }
}

@AliceDsl
fun AliceHandler.command(vararg commands: String, body: Handler) {
    createCommand(*commands, body = body)
}

@AliceDsl
fun AliceHandler.welcome(body: Handler) {
    command("", body = body)
}

@AliceDsl
fun AliceHandler.default(body: Handler) {
    registerUnknownCommand(body)
}

suspend fun aliceHandle(aliceFullRequest: AliceFullRequest): AliceResponse {
    return AliceHandler.handle(aliceFullRequest)
}

@AliceDsl
fun aliceRouting(body: AliceHandler.() -> Unit) {
    AliceHandler.body()
}