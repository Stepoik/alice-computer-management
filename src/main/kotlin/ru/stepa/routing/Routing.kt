package ru.stepa.routing

import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.launch
import org.koin.mp.KoinPlatform
import ru.stepa.alice.core.*

val VALORANT_START_ROUTING = arrayOf("запусти valorant")

fun Application.configureRouting() {
    routing {
        post("/") {
            val aliceFullRequest = call.receive<AliceFullRequest>()
            val response = aliceHandle(aliceFullRequest)
            call.respond(response)
        }
    }

    aliceRouting {
        val channel: SendChannel<AliceFullRequest> = KoinPlatform.getKoin().get()
        welcome { _ ->
            Response(
                text = "Рад вас видеть",
                endSession = false
            )
        }

        command(*VALORANT_START_ROUTING) { aliceRequest ->
            launch {
                channel.send(aliceRequest)
            }
            Response(
                text = "Запускаю valorant",
                endSession = false
            )
        }
    }
}
