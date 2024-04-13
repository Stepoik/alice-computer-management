package ru.stepa.services

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.util.*
import org.koin.dsl.module
import ru.stepa.alice.core.AliceFullRequest


object Constants {
//    val WEBSOCKET_SERVICE_URL = System.getenv("WEBSOCKET_SERVICE_URL")
    val WEBSOCKET_SERVICE_URL = "http://root-hub.ru:41031/send"
}

val redirectModule = module {
    single { RedirectService() }
}

class RedirectService {
    suspend fun sendToWebsocketService(aliceFullRequest: AliceFullRequest) {
        httpClient().use {
            it.post(Constants.WEBSOCKET_SERVICE_URL) {
                contentType(ContentType.Application.Json)
                setBody(aliceFullRequest)
            }
        }
    }

    private fun httpClient() = HttpClient(CIO) {
        install(ContentNegotiation) {
            json()
        }
    }
}