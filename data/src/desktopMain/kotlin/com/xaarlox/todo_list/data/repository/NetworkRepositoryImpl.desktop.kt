package com.xaarlox.todo_list.data.repository

import com.xaarlox.todo_list.domain.repository.NetworkRepository

actual class NetworkRepositoryImpl : NetworkRepository {
    actual override suspend fun getUserIp(): String {
        TODO("Not yet implemented")
    }
}