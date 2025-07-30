package com.xaarlox.todo_list.ui.viewmodels.mvi

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.xaarlox.todo_list.domain.model.Todo
import com.xaarlox.todo_list.ui.util.UiEvent
import com.xaarlox.todo_list.ui.viewmodels.FakeTodoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class EditTodoViewModelTest {
    private lateinit var fakeRepository: FakeTodoRepository
    private lateinit var savedStateHandle: SavedStateHandle
    private lateinit var editTodoViewModel: EditTodoViewModel

    private var testDispatcher = StandardTestDispatcher()

    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        fakeRepository = FakeTodoRepository()
        editTodoViewModel = createViewModel()
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun changeTitle_shouldUpdateTitleInState() = runTest {
        editTodoViewModel.onIntent(EditTodoIntent.OnTitleChanged("Test title"))
        assertEquals("Test title", editTodoViewModel.state.value.title)
    }

    @Test
    fun changeDescription_shouldUpdateDescriptionInState() =
        runTest {
            editTodoViewModel.onIntent(EditTodoIntent.OnDescriptionChanged("Test description"))
            assertEquals("Test description", editTodoViewModel.state.value.description)
        }

    @Test
    fun changeIsDone_shouldUpdateIsDoneInState() = runTest {
        editTodoViewModel.onIntent(EditTodoIntent.OnIsDoneChanged(true))
        assertTrue(editTodoViewModel.state.value.isDone)
    }

    @Test
    fun saveTodoWithBlankTitle_shouldEmitSnackbarWithErrorMessage() = runTest {
        editTodoViewModel.onIntent(EditTodoIntent.OnTitleChanged(""))
        editTodoViewModel.uiEffect.test {
            editTodoViewModel.onIntent(EditTodoIntent.OnSaveTodoClick)
            val event = awaitItem()
            assertTrue(event is UiEvent.ShowSnackBar)
            assertEquals("The title can't be empty", event.message)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun saveTodoWithValidData_shouldSaveTodoAndEmitSuccessAndNavigationEvents() =
        runTest {
            editTodoViewModel.onIntent(EditTodoIntent.OnTitleChanged("Title"))
            editTodoViewModel.onIntent(EditTodoIntent.OnDescriptionChanged("Description"))
            editTodoViewModel.onIntent(EditTodoIntent.OnIsDoneChanged(true))
            editTodoViewModel.onIntent(EditTodoIntent.OnSaveTodoClick)

            testDispatcher.scheduler.advanceUntilIdle()

            val todos = fakeRepository.getTodos().first()
            assertTrue(todos.any() { it.title == "Title" && it.description == "Description" && it.isDone })

            editTodoViewModel.uiEffect.test {
                assertEquals(
                    "Todo saved successfully",
                    (awaitItem() as UiEvent.ShowSnackBar).message
                )
                assertTrue(awaitItem() is UiEvent.PopBackStack)
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun initializedWithTodoId_shouldLoadTodoFromRepository() = runTest {
        val todo = Todo(1, "Title", "Description", false)
        fakeRepository.insertTodo(todo)

        val viewModel = createViewModel(todoId = 1)
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(1, viewModel.state.value.id)
        assertEquals("Title", viewModel.state.value.title)
        assertEquals("Description", viewModel.state.value.description)
        assertFalse(viewModel.state.value.isDone)
    }

    private fun createViewModel(todoId: Int? = null): EditTodoViewModel {
        val handle =
            if (todoId != null) SavedStateHandle(mapOf("todoId" to todoId)) else SavedStateHandle()
        savedStateHandle = handle
        return EditTodoViewModel(fakeRepository, handle)
    }
}