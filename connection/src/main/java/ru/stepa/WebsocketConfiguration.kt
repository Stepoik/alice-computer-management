package ru.stepa

import io.ktor.serialization.kotlinx.*
import io.ktor.server.application.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.serialization.json.Json
import org.koin.mp.KoinPlatform
import ru.stepa.alice.core.AliceFullRequest
import java.time.Duration

fun Application.configureSockets() {
    install(WebSockets) {
        pingPeriod = Duration.ofSeconds(15)
        timeout = Duration.ofSeconds(15)
        maxFrameSize = Long.MAX_VALUE
        masking = false
        contentConverter = KotlinxWebsocketSerializationConverter(Json)
    }
    routing {
        val commandsFlow: SharedFlow<AliceFullRequest> = KoinPlatform.getKoin().get()
        webSocket("/ws") {
            val collectScope = CoroutineScope(Dispatchers.Default)
            collectScope.launch {
                commandsFlow.collect { request ->
                    runCatching {
                        sendSerialized(request)
                    }.onFailure {
                        collectScope.cancel()
                    }
                }
            }.join()
            close()
        }
    }
}
