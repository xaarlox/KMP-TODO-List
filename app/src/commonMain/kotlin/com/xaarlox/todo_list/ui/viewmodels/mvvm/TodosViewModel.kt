package com.xaarlox.todo_list.ui.viewmodels.mvvm

import androidx.lifecycle.ViewModel
import com.xaarlox.todo_list.ui.util.UiEvent
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

class TodosViewModel() : ViewModel() {
    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()
}