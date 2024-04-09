package ru.stepa

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.SendChannel
import org.koin.core.context.startKoin
import org.koin.dsl.module
import ru.stepa.alice.core.AliceFullRequest
import ru.stepa.plugins.*
import ru.stepa.routing.configureRouting
import ru.stepa.websocket.configureSockets

val channelModule = module {
    val channel = Channel<AliceFullRequest>()
    single<ReceiveChannel<AliceFullRequest>> { channel }
    single<SendChannel<AliceFullRequest>> { channel }
}

fun main() {
    startKoin {
        modules(channelModule)
    }
    embeddedServer(Netty, port = 41031, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    configureSerialization()
    configureSockets()
    configureRouting()
}
