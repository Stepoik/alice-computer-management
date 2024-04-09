package ru.stepa.alice.core

typealias Handler = suspend (AliceFullRequest) -> Response

@DslMarker
annotation class AliceDsl

object AliceHandler {
    private val registeredCommands = mutableMapOf<String, Handler>()

    internal fun createCommand(vararg commands: String, body: Handler) {
        for (i in commands) {
            registeredCommands[i] = body
        }
    }

    suspend fun handle(aliceFullRequest: AliceFullRequest): AliceResponse {
        val command = aliceFullRequest.request.command
        println("Is command empty: ${command.isEmpty()}")
        println(registeredCommands.containsKey(command))
        val handler = registeredCommands[command] ?: return AliceResponse.unknownCommand(aliceFullRequest)
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

suspend fun aliceHandle(aliceFullRequest: AliceFullRequest): AliceResponse {
    return AliceHandler.handle(aliceFullRequest)
}

@AliceDsl
fun aliceRouting(body: AliceHandler.() -> Unit) {
    AliceHandler.body()
}