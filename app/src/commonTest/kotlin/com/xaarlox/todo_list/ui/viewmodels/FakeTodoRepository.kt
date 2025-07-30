package com.xaarlox.todo_list.ui.viewmodels

import com.xaarlox.todo_list.domain.model.Todo
import com.xaarlox.todo_list.domain.repository.TodoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class FakeTodoRepository : TodoRepository {
    private val todosFlow = MutableStateFlow<List<Todo>>(emptyList())

    override suspend fun insertTodo(todo: Todo) {
        todosFlow.value = todosFlow.value.filterNot { it.id == todo.id } + todo
    }

    override suspend fun deleteTodo(todo: Todo) {
        todosFlow.value = todosFlow.value.filterNot { it.id == todo.id }
    }

    override suspend fun getTodoById(id: Int): Todo? {
        return todosFlow.value.find { it.id == id }
    }

    override fun getTodos(): Flow<List<Todo>> = todosFlow

    fun addTodo(todo: Todo) {
        todosFlow.value += todo
    }
}