package com.xaarlox.todo_list.data.repository

import com.xaarlox.todo_list.domain.repository.NetworkRepository

class NetworkRepositoryImpl(
) : NetworkRepository {
    override suspend fun getUserIp(): String {
        return "development of the network repository is in progress"
    }
}