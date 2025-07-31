package com.xaarlox.todo_list.ui.di

import androidx.lifecycle.SavedStateHandle
import com.xaarlox.todo_list.ui.viewmodels.mvi.EditTodoViewModel
import com.xaarlox.todo_list.ui.viewmodels.mvvm.TodosViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val uiModule = module {
    viewModelOf(::TodosViewModel)
    viewModel { (handle: SavedStateHandle) ->
        EditTodoViewModel(
            todoRepository = get(),
            savedStateHandle = handle
        )
    }
}