package com.xaarlox.todo_list.data.di

import com.xaarlox.todo_list.data.repository.TodoRepository
import org.koin.dsl.module

val dataModule = module {
    single {
        TodoRepository()
    }
}