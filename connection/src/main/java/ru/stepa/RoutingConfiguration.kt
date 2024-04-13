package ru.stepa

import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.flow.MutableSharedFlow
import org.koin.mp.KoinPlatform
import ru.stepa.alice.core.AliceFullRequest

fun Application.configureRouting() {
    val channel: MutableSharedFlow<AliceFullRequest> = KoinPlatform.getKoin().get()
    routing {
        post("/send") {
            val request = call.receive<AliceFullRequest>()
            channel.emit(request)
        }
    }
}