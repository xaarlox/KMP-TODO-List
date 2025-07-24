package com.xaarlox.todo_list.data.di

import org.koin.core.module.Module
import org.koin.dsl.module

val dataModule = module {
    includes(platformModule())
}

expect fun platformModule(): Module