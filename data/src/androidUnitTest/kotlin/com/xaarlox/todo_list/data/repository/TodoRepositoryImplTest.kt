package com.xaarlox.todo_list.data.repository

import com.xaarlox.todo_list.data.local.TodoDao
import com.xaarlox.todo_list.data.local.entity.TodoEntity
import com.xaarlox.todo_list.domain.model.Todo
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.just
import io.mockk.runs
import io.mockk.verify
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test


class TodoRepositoryImplTest {
    @MockK
    private lateinit var todoDao: TodoDao
    private lateinit var todoRepositoryImpl: TodoRepositoryImpl

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        todoRepositoryImpl = TodoRepositoryImpl(todoDao)
    }

    @Test
    fun `should insert mapped entity to DAO when insertTodo is called with domain model`() =
        runBlocking {
            val todo = Todo(
                id = 1,
                title = "Start Challenge",
                description = "Start GET TONED challenge (Chloe Ting)",
                isDone = true
            )
            coEvery { todoDao.insertToDo(any()) } just runs
            todoRepositoryImpl.insertTodo(todo)
            coVerify {
                todoDao.insertToDo(match {
                    it.id == todo.id && it.title == todo.title && it.description == todo.description && it.isDone == todo.isDone
                })
            }
        }

    @Test
    fun `should delete mapped entity from DAO when deleteTodo is called with domain model`() =
        runBlocking {
            val todo = Todo(
                id = 1,
                title = "Nothing beats a jet 2 holiday",
                description = "And right now you can save 50 pounds per person",
                isDone = true
            )
            coEvery { todoDao.deleteTodo(any()) } just runs
            todoRepositoryImpl.deleteTodo(todo)
            coVerify {
                todoDao.deleteTodo(match {
                    it.id == todo.id && it.title == todo.title && it.description == todo.description && it.isDone == todo.isDone
                })
            }
        }

    @Test
    fun `should return mapped domain model when entity exists in DAO for given id`() = runBlocking {
        val todoId = 1
        val entity = TodoEntity(todoId, "Organize workspace", "Clean and declutter your desk", true)
        coEvery { todoDao.getTodoById(todoId) } returns entity
        val result = todoRepositoryImpl.getTodoById(todoId)
        assertEquals(
            Todo(todoId, "Organize workspace", "Clean and declutter your desk", true), result
        )
        coVerify { todoDao.getTodoById(todoId) }
    }

    @Test
    fun `should return null when no entity is found in DAO for given id`() = runBlocking {
        val todoId = 1
        coEvery { todoDao.getTodoById(todoId) } returns null
        val result = todoRepositoryImpl.getTodoById(todoId)
        assertNull(result)
        coVerify { todoDao.getTodoById(todoId) }
    }

    @Test
    fun `should return mapped list of domain models when DAO returns entities`() = runBlocking {
        val todoEntities = listOf(
            TodoEntity(1, "Learn a phrase", "He who denied it, supplied it", true), TodoEntity(
                2,
                "Paul Mescal movie marathon",
                "Watch all the films starring Paul Mescal, starting with Aftersun",
                false
            )
        )
        every { todoDao.getTodos() } returns flowOf(todoEntities)
        val todos = todoRepositoryImpl.getTodos().first()
        assertEquals(2, todos.size)
        assertEquals(Todo(1, "Learn a phrase", "He who denied it, supplied it", true), todos[0])
        assertEquals(
            Todo(
                2,
                "Paul Mescal movie marathon",
                "Watch all the films starring Paul Mescal, starting with Aftersun",
                false
            ), todos[1]
        )
        verify { todoDao.getTodos() }
    }
}