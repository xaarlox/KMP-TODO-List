package com.xaarlox.todo_list.data.di

import com.xaarlox.todo_list.domain.repository.NetworkRepository
import com.xaarlox.todo_list.data.repository.NetworkRepositoryImpl
import com.xaarlox.todo_list.data.repository.TodoRepositoryImpl
import com.xaarlox.todo_list.domain.repository.TodoRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

actual fun platformModule() = module {
    singleOf(::NetworkRepositoryImpl).bind<NetworkRepository>()
    singleOf(::TodoRepositoryImpl).bind<TodoRepository>()
}