package com.xaarlox.todo_list.data.repository

import com.xaarlox.todo_list.domain.model.Todo
import com.xaarlox.todo_list.domain.repository.TodoRepository
import kotlinx.coroutines.flow.Flow

expect class TodoRepositoryImpl : TodoRepository {
    override suspend fun insertTodo(todo: Todo)
    override suspend fun deleteTodo(todo: Todo)
    override suspend fun getTodoById(id: Int): Todo?
    override fun getTodos(): Flow<List<Todo>>
}