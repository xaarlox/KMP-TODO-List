package com.xaarlox.todo_list.ui.screens

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsOff
import androidx.compose.ui.test.assertIsOn
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.runComposeUiTest
import androidx.lifecycle.SavedStateHandle
import com.xaarlox.todo_list.domain.model.Todo
import com.xaarlox.todo_list.ui.viewmodels.FakeTodoRepository
import com.xaarlox.todo_list.ui.viewmodels.mvi.EditTodoIntent
import com.xaarlox.todo_list.ui.viewmodels.mvi.EditTodoViewModel
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertTrue

@OptIn(ExperimentalTestApi::class)
class EditTodoScreenTest {
    private lateinit var fakeTodoRepository: FakeTodoRepository
    private lateinit var viewModel: EditTodoViewModel
    private val initialTodo =
        Todo(id = 1, title = "Test title", description = "Test description", isDone = false)

    @BeforeTest
    fun setUp() {
        fakeTodoRepository = FakeTodoRepository()
        fakeTodoRepository.addTodo(initialTodo)
        viewModel = EditTodoViewModel(fakeTodoRepository, SavedStateHandle(mapOf("todoId" to 1)))
    }

    @Test
    fun setEditTodoScreenContent_shouldDisplayAllFields() = runComposeUiTest {
        setContent {
            EditTodoScreen(viewModel = viewModel, onPopBackStack = {})
        }
        onNodeWithText("Test title").assertExists()
        onNodeWithText("Test description").assertExists()
        onNodeWithText("Mark as complete").assertExists()
        onNodeWithContentDescription("Save").assertExists()
    }

    @Test
    fun typingTitle_shouldTriggerOnTitleChangedIntent() = runComposeUiTest {
        setContent {
            EditTodoScreen(viewModel = viewModel, onPopBackStack = {})
        }
        onNodeWithText("Test title").performClick().performTextInput(" new")
        onNodeWithText("Test title new").assertIsDisplayed()
    }

    @Test
    fun typingDescription_shouldTriggerOnDescriptionChangedIntent() = runComposeUiTest {
        setContent {
            EditTodoScreen(viewModel = viewModel, onPopBackStack = {})
        }
        onNodeWithText("Test description").performClick().performTextInput(" more")
        onNodeWithText("Test description more").assertIsDisplayed()
    }

    @Test
    fun clickingCheckbox_shouldTriggerOnIsDoneChangedIntent() = runComposeUiTest {
        setContent {
            EditTodoScreen(viewModel = viewModel, onPopBackStack = {})
        }
        val checkboxNode = onNodeWithContentDescription("Checkbox")
        checkboxNode.performClick()
        checkboxNode.assertIsOn()
        checkboxNode.performClick()
        checkboxNode.assertIsOff()
    }

    @Test
    fun clickingSaveFab_shouldTriggerOnSaveIntentAndShowSnackbar() = runComposeUiTest {
        setContent {
            EditTodoScreen(viewModel = viewModel, onPopBackStack = {})
        }
        onNodeWithContentDescription("Save").performClick()
        waitUntil(timeoutMillis = 5000) {
            onAllNodesWithText("Todo saved successfully").fetchSemanticsNodes().isNotEmpty()
        }
        onNodeWithText("Todo saved successfully").assertIsDisplayed()
    }

    @Test
    fun emitPopBackStackEvent_shouldTriggerNavigationBack() = runComposeUiTest {
        var popped = false
        setContent {
            EditTodoScreen(viewModel = viewModel, onPopBackStack = { popped = true })
        }
        viewModel.onIntent(EditTodoIntent.OnSaveTodoClick)
        waitUntil(timeoutMillis = 5000) { popped }
        assertTrue(popped)
    }
}