package com.xaarlox.todo_list.ui.screens

import androidx.compose.ui.test.ComposeUiTest
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.runComposeUiTest
import androidx.compose.ui.test.swipeLeft
import androidx.compose.ui.test.waitUntilDoesNotExist
import com.xaarlox.todo_list.domain.model.Todo
import com.xaarlox.todo_list.ui.util.UiEvent
import com.xaarlox.todo_list.ui.viewmodels.FakeNetworkRepository
import com.xaarlox.todo_list.ui.viewmodels.FakeTodoRepository
import com.xaarlox.todo_list.ui.viewmodels.mvvm.TodosEvent
import com.xaarlox.todo_list.ui.viewmodels.mvvm.TodosViewModel
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

@OptIn(ExperimentalTestApi::class)
class TodosScreenTest {
    private lateinit var fakeTodoRepository: FakeTodoRepository
    private lateinit var fakeNetworkRepository: FakeNetworkRepository
    private lateinit var viewModel: TodosViewModel

    private val testTodo =
        Todo(id = 1, title = "Title1", description = "Description1", isDone = false)

    @BeforeTest
    fun setUp() {
        fakeTodoRepository = FakeTodoRepository()
        fakeNetworkRepository = FakeNetworkRepository()
        fakeTodoRepository.addTodo(testTodo)
        viewModel = TodosViewModel(fakeTodoRepository, fakeNetworkRepository)
    }

    @Test
    fun setTodosScreenContent_shouldDisplayTodosAndIp() = runComposeUiTest {
        setTodosScreenContent(this)
        onNodeWithText("Title1").assertIsDisplayed()
        onNodeWithText("Your IP: 15.03.20.24").assertIsDisplayed()
    }

    @Test
    fun swipeLeftOnTodo_shouldTriggerDeleteEvent() = runComposeUiTest {
        setTodosScreenContent(this)
        onNodeWithTag("swipe-Title1").assertIsDisplayed()
        onNodeWithTag("swipe-Title1").performTouchInput { swipeLeft() }
        waitUntilDoesNotExist(hasText("Title1"))
    }

    @Test
    fun emitShowSnackBarEvent_shouldDisplaySnackbar() = runComposeUiTest {
        setTodosScreenContent(this)
        viewModel.onEvent(TodosEvent.OnDoneChange(testTodo, isDone = true))
        waitUntil(timeoutMillis = 5000) {
            onAllNodesWithText(
                "Congrats! Todo completed!",
                useUnmergedTree = true
            ).fetchSemanticsNodes().isNotEmpty()
        }
        onNodeWithText("Congrats! Todo completed!", useUnmergedTree = true).assertIsDisplayed()
    }

    @Test
    fun todosScreen_addButton_triggers_navigation() = runComposeUiTest {
        var navigatedEvent: UiEvent.Navigate? = null
        setTodosScreenContent(this, onNavigate = { event -> navigatedEvent = event })
        onNodeWithContentDescription("Add").performClick()
        waitUntil(timeoutMillis = 5000) { navigatedEvent != null }
        assertTrue(navigatedEvent is UiEvent.Navigate)
        assertTrue((navigatedEvent as UiEvent.Navigate).route == "edit_todo")
    }

    @Test
    fun clickEditButton_shouldTriggerNavigationToEditScreen() = runComposeUiTest {
        var navigatedEvent: UiEvent.Navigate? = null
        setTodosScreenContent(this, onNavigate = { event -> navigatedEvent = event })

        onNodeWithContentDescription("Edit Title1").performClick()
        waitUntil(timeoutMillis = 5000) { navigatedEvent != null }
        assertNotNull(navigatedEvent)
        assertTrue(navigatedEvent is UiEvent.Navigate)
        assertEquals("edit_todo?todoId=1", (navigatedEvent as UiEvent.Navigate).route)
    }

    private fun setTodosScreenContent(
        testScope: ComposeUiTest,
        onNavigate: (UiEvent.Navigate) -> Unit = {},
        viewModel: TodosViewModel = this.viewModel
    ) {
        testScope.setContent {
            TodosScreen(
                onNavigate = onNavigate,
                viewModel = viewModel
            )
        }
    }
}