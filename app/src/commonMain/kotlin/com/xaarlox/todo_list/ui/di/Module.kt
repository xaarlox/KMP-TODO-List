package com.xaarlox.todo_list.ui.di

import com.xaarlox.todo_list.ui.viewmodels.mvvm.TodosViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val uiModule = module {
    viewModelOf(::TodosViewModel)
}