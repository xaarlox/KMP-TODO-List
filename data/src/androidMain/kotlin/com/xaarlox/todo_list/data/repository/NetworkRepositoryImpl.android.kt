package com.xaarlox.todo_list.data.repository

import com.xaarlox.todo_list.domain.repository.NetworkRepository
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

actual class NetworkRepositoryImpl : NetworkRepository {
    private val client = createHttpClient()

    actual override suspend fun getUserIp(): String {
        return client.get("https://api.ipify.org/").bodyAsText()
    }
}

internal fun createHttpClient(): HttpClient {
    return HttpClient(Android) {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                prettyPrint = true
                isLenient = true
            })
        }
    }
}