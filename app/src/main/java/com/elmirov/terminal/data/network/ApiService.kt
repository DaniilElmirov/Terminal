package com.elmirov.terminal.data.network

import com.elmirov.terminal.data.model.Result
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {

    @GET("aggs/ticker/AAPL/range/{timeFrame}/2022-01-09/2023-01-09?adjusted=true&sort=desc&limit=50000&apiKey=jYdjt7h3fnLlrAD1ii00ZWtyQTg8zAh7")
    suspend fun getBars(
        @Path("timeFrame") timeFrame: String,
    ): Result
}