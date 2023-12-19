package com.elmirov.terminal.data.network

import com.elmirov.terminal.data.model.Result
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.logging.SIMPLE
import io.ktor.client.request.get
import io.ktor.serialization.gson.gson

object KtorClient {

    private const val BASE_URL = "https://api.polygon.io/v2/"

    private val client = HttpClient(Android) {
        defaultRequest {
            url(BASE_URL)
        }

        install(Logging) {
            logger = Logger.SIMPLE
        }

        install(ContentNegotiation) {
//            json(Json {
//                ignoreUnknownKeys = true
//            })
            gson()
        }
    }

    suspend fun getBars(timeFrame: String): Result =
        client
            .get("aggs/ticker/AAPL/range/$timeFrame/2022-01-09/2023-01-09?adjusted=true&sort=desc&limit=50000&apiKey=jYdjt7h3fnLlrAD1ii00ZWtyQTg8zAh7")
            .body()
}