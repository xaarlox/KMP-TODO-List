package com.xaarlox.todo_list.data.repository

import kotlin.collections.mutableListOf
import com.xaarlox.todo_list.domain.model.Todo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

actual class TodoRepository {
    private val dummyTodo = mutableListOf(
        Todo(id = 1, title = "todo1", description = "todo", isDone = false),
        Todo(id = 2, title = "todo2", description = "todo", isDone = false),
        Todo(id = 3, title = "todo3", description = "todo", isDone = false),
        Todo(id = 4, title = "todo4", description = "todo", isDone = false)
    )
    private val _todosFlow = MutableStateFlow(dummyTodo.toList())

    actual suspend fun insertTodo(todo: Todo) {
        val newTodo = if ((todo.id ?: 0) <= 0) {
            val newId = (dummyTodo.mapNotNull { it.id }.maxOrNull() ?: 0) + 1
            todo.copy(id = newId)
        } else {
            todo
        }
        dummyTodo.add(newTodo)
        _todosFlow.update { dummyTodo.toList() }
    }

    actual suspend fun deleteTodo(todo: Todo) {
        dummyTodo.removeIf { it.id == todo.id }
        _todosFlow.update { dummyTodo.toList() }
    }

    actual suspend fun getTodoById(id: Int): Todo? {
        return dummyTodo.find { it.id == id }
    }

    actual fun getTodos(): Flow<List<Todo>> = _todosFlow
}