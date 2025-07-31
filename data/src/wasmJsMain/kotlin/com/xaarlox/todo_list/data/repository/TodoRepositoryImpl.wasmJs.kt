package com.xaarlox.todo_list.data.repository

import com.xaarlox.todo_list.domain.model.Todo
import com.xaarlox.todo_list.domain.repository.TodoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

actual class TodoRepositoryImpl : TodoRepository {
    private val sampleTodos = mutableListOf(
        Todo(id = 1, title = "Buy groceries", description = "Milk, bread, eggs", isDone = false),
        Todo(id = 2, title = "Read a book", description = "Read 20 pages", isDone = false),
        Todo(
            id = 3,
            title = "Run 5 km",
            description = "Go for a morning run in the park",
            isDone = false
        ),
        Todo(id = 4, title = "Watch a movie", description = "Watch 'Aftersun'", isDone = false)
    )
    private val _todosState = MutableStateFlow(sampleTodos.toList())

    actual override suspend fun insertTodo(todo: Todo) {
        todo.id?.takeIf { it > 0 }?.let { id ->
            val index = sampleTodos.indexOfFirst { it.id == id }
            if (index != -1) {
                sampleTodos[index] = todo
            } else {
                sampleTodos.add(todo)
            }
        } ?: run {
            val newId = (sampleTodos.mapNotNull { it.id }.maxOrNull() ?: 0) + 1
            sampleTodos.add(todo.copy(id = newId))
        }
        _todosState.update { sampleTodos.toList() }
    }

    actual override suspend fun deleteTodo(todo: Todo) {
        val index = sampleTodos.indexOfFirst { it.id == todo.id }
        if (index != -1) {
            sampleTodos.removeAt(index)
            _todosState.update { sampleTodos.toList() }
        }
    }

    actual override suspend fun getTodoById(id: Int): Todo? {
        return sampleTodos.find { it.id == id }
    }

    actual override fun getTodos(): Flow<List<Todo>> = _todosState
}