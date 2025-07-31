package com.xaarlox.todo_list.data.repository

import com.xaarlox.todo_list.domain.repository.NetworkRepository

expect class NetworkRepositoryImpl : NetworkRepository {
    override suspend fun getUserIp(): String
}