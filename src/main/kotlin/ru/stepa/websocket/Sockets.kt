package ru.stepa.websocket

import io.ktor.serialization.kotlinx.*
import io.ktor.server.application.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.consumeEach
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
        val commandsChannel: ReceiveChannel<AliceFullRequest> = KoinPlatform.getKoin().get()
        webSocket("/ws") {
            send("Please enter your name")
            for (frame in incoming) {
                frame as? Frame.Text ?: continue
                val receivedText = frame.readText()
                if (receivedText.equals("bye", ignoreCase = true)) {
                    close(CloseReason(CloseReason.Codes.NORMAL, "Client said BYE"))
                } else {
                    send(Frame.Text("Hi, $receivedText!"))
                }
            }
//            send(Frame.Text("hello"))
//            commandsChannel.consumeEach { request ->
//                sendSerialized(request)
//            }
        }
    }
}
