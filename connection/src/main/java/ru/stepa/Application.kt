package ru.stepa

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import org.koin.core.context.startKoin
import org.koin.dsl.module
import ru.stepa.alice.core.AliceFullRequest

val channelModule = module {
    val flow = MutableSharedFlow<AliceFullRequest>()
    single<SharedFlow<AliceFullRequest>> { flow }
    single<MutableSharedFlow<AliceFullRequest>> { flow }
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