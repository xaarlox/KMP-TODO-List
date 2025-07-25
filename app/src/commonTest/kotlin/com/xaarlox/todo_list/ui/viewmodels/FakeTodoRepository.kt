package com.xaarlox.todo_list.ui.viewmodels

import com.xaarlox.todo_list.domain.model.Todo
import com.xaarlox.todo_list.domain.repository.TodoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeTodoRepository : TodoRepository {
    private val initialTodos = mutableListOf<Todo>()

    override suspend fun insertTodo(todo: Todo) {
        initialTodos.add(todo)
    }

    override suspend fun deleteTodo(todo: Todo) {
        initialTodos.remove(todo)
    }

    override suspend fun getTodoById(id: Int): Todo? {
        return initialTodos.find { it.id == id }
    }

    override fun getTodos(): Flow<List<Todo>> {
        return flow { emit(initialTodos) }
    }
}