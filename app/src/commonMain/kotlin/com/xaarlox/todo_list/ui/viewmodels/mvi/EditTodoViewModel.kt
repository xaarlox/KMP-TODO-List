package com.xaarlox.todo_list.ui.viewmodels.mvi

import androidx.lifecycle.ViewModel
import com.xaarlox.todo_list.ui.util.UiEvent
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow

class EditTodoViewModel() : ViewModel() {
    private val _state = MutableStateFlow(EditTodoState())
    val state: StateFlow<EditTodoState> = _state.asStateFlow()

    private val _uiEffect = Channel<UiEvent>()
    val uiEffect = _uiEffect.receiveAsFlow()

    fun onIntent(intent: EditTodoIntent) {

    }
}