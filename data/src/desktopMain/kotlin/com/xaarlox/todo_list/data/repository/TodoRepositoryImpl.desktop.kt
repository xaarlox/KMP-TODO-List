package com.xaarlox.todo_list.data.repository

import com.xaarlox.todo_list.data.local.TodoDao
import com.xaarlox.todo_list.data.local.entity.TodoEntity
import com.xaarlox.todo_list.domain.model.Todo
import com.xaarlox.todo_list.domain.repository.TodoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

actual class TodoRepositoryImpl(
    private val dao: TodoDao
) : TodoRepository {
    actual override suspend fun insertTodo(todo: Todo) {
        dao.insertToDo(TodoEntity.fromDomainModel(todo))
    }

    actual override suspend fun deleteTodo(todo: Todo) {
        dao.deleteTodo(TodoEntity.fromDomainModel(todo))
    }

    actual override suspend fun getTodoById(id: Int): Todo? {
        return dao.getTodoById(id)?.toDomainModel()
    }

    actual override fun getTodos(): Flow<List<Todo>> {
        return dao.getTodos().map { entities -> entities.map { it.toDomainModel() } }
    }
}