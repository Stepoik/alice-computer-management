package ru.stepa.routing

import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.launch
import org.koin.mp.KoinPlatform
import ru.stepa.alice.core.*
import ru.stepa.services.RedirectService

val VALORANT_START_ROUTING = arrayOf("запусти valorant")

fun Application.configureRouting() {
    val redirectService = KoinPlatform.getKoin().get<RedirectService>()
    routing {
        post("/") {
            val aliceFullRequest = call.receive<AliceFullRequest>()
            call.respond(aliceHandle(aliceFullRequest))
        }
    }

    aliceRouting {
        welcome { _ ->
            Response(
                text = "Рад вас видеть",
                endSession = false
            )
        }

        command(*VALORANT_START_ROUTING) { aliceRequest ->
            redirectService.sendToWebsocketService(aliceRequest)
            Response(
                text = "Запускаю valorant",
                endSession = false
            )
        }

        default {aliceRequest ->
            redirectService.sendToWebsocketService(aliceRequest)
            Response(
                text = "Выполняю вышу команду",
                endSession = false
            )
        }

    }
}
