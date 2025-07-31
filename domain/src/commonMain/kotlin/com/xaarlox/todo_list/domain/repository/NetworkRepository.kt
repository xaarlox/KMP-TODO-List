package com.xaarlox.todo_list.domain.repository

interface NetworkRepository {
    suspend fun getUserIp(): String
}