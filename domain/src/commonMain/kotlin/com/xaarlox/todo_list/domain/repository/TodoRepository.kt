package com.xaarlox.todo_list.domain.repository

import com.xaarlox.todo_list.domain.model.Todo
import kotlinx.coroutines.flow.Flow

interface TodoRepository {
    suspend fun insertTodo(todo: Todo)
    suspend fun deleteTodo(todo: Todo)
    suspend fun getTodoById(id: Int): Todo?
    fun getTodos(): Flow<List<Todo>>
}