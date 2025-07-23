package com.xaarlox.todo_list.data.di

import com.xaarlox.todo_list.domain.repository.NetworkRepository
import com.xaarlox.todo_list.data.repository.NetworkRepositoryImpl
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

actual fun platformModule() = module {
    singleOf(::NetworkRepositoryImpl).bind<NetworkRepository>()
}