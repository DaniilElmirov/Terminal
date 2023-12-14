package com.elmirov.terminal.data.network

import com.elmirov.terminal.data.model.Result
import retrofit2.http.GET

interface ApiService {

    @GET("aggs/ticker/AAPL/range/1/hour/2022-01-09/2023-01-09?adjusted=true&sort=desc&limit=50000&apiKey=jYdjt7h3fnLlrAD1ii00ZWtyQTg8zAh7")
    suspend fun getBars(): Result
}