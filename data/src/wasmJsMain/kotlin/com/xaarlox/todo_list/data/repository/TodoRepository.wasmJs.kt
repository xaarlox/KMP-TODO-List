package com.xaarlox.todo_list.data.repository

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
        todo.id?.takeIf { it > 0 }?.let { id ->
            val index = dummyTodo.indexOfFirst { it.id == id }
            if (index != -1) {
                dummyTodo[index] = todo
            } else {
                dummyTodo.add(todo)
            }
        } ?: run {
            val newId = (dummyTodo.mapNotNull { it.id }.maxOrNull() ?: 0) + 1
            dummyTodo.add(todo.copy(id = newId))
        }
        _todosFlow.update { dummyTodo.toList() }
    }

    actual suspend fun deleteTodo(todo: Todo) {
        val index = dummyTodo.indexOfFirst { it.id == todo.id }
        if (index != -1) {
            dummyTodo.removeAt(index)
            _todosFlow.update { dummyTodo.toList() }
        }
    }

    actual suspend fun getTodoById(id: Int): Todo? {
        return dummyTodo.find { it.id == id }
    }

    actual fun getTodos(): Flow<List<Todo>> = _todosFlow
}