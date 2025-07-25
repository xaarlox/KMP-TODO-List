package com.xaarlox.todo_list.ui.viewmodels

import com.xaarlox.todo_list.domain.repository.NetworkRepository

open class FakeNetworkRepository : NetworkRepository {
    private val fakeUserIp = "15.03.20.24"

    override suspend fun getUserIp(): String {
        return fakeUserIp
    }
}