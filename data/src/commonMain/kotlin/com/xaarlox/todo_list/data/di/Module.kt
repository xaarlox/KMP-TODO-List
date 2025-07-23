package com.xaarlox.todo_list.data.di

import com.xaarlox.todo_list.data.repository.TodoRepository
import com.xaarlox.todo_list.domain.repository.NetworkRepository
import com.xaarlox.todo_list.data.repository.NetworkRepositoryImpl
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val dataModule = module {
    includes(platformModule())
    singleOf(::TodoRepository)
    //singleOf(::NetworkRepositoryImpl).bind<NetworkRepository>()
}

expect fun platformModule(): Module