import app.cash.turbine.test
import com.xaarlox.todo_list.domain.model.Todo
import com.xaarlox.todo_list.ui.util.Routes
import com.xaarlox.todo_list.ui.util.UiEvent
import com.xaarlox.todo_list.ui.viewmodels.FakeNetworkRepository
import com.xaarlox.todo_list.ui.viewmodels.FakeTodoRepository
import com.xaarlox.todo_list.ui.viewmodels.mvvm.TodosEvent
import com.xaarlox.todo_list.ui.viewmodels.mvvm.TodosViewModel
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
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class TodosViewModelTest {
    private lateinit var fakeTodoRepository: FakeTodoRepository
    private lateinit var fakeNetworkRepository: FakeNetworkRepository

    private lateinit var viewModel: TodosViewModel
    private var testDispatcher = StandardTestDispatcher()

    private val testTodo = Todo(1, "Test title", "Test description", false)
    private val completedTodo = testTodo.copy(isDone = true)

    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        fakeTodoRepository = FakeTodoRepository()
        fakeNetworkRepository = FakeNetworkRepository()
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun initializeViewModel_shouldEmitInitialEmptyTodos() = runTest {
        createViewModel()
        viewModel.todos.test {
            assertEquals(emptyList(), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun initializeViewModel_shouldFetchAndDisplayUserIp() = runTest {
        createViewModel()
        testDispatcher.scheduler.advanceUntilIdle()
        viewModel.userIp.test {
            assertEquals("15.03.20.24", awaitItem())
        }
    }

    @Test
    fun fetchUserIpWithNetworkError_shouldSetErrorMessage() = runTest {
        val errorNetworkRepository = object : FakeNetworkRepository() {
            override suspend fun getUserIp(): String {
                throw RuntimeException("Network error")
            }
        }
        viewModel = TodosViewModel(fakeTodoRepository, errorNetworkRepository)
        testDispatcher.scheduler.runCurrent()
        viewModel.userIp.test {
            assertEquals("Error: Network error", awaitItem())
            cancel()
        }
    }

    @Test
    fun clickOnTodo_shouldEmitNavigateUiEventWithTodoId() = runTest {
        createViewModel()
        expectSingleUiEvent(UiEvent.Navigate(Routes.EDIT_TODO + "?todoId=${testTodo.id}")) {
            viewModel.onEvent(TodosEvent.OnTodoClick(testTodo))
        }
    }

    @Test
    fun clickOnAddTodo_shouldEmitNavigateUiEventWithoutId() = runTest {
        createViewModel()
        expectSingleUiEvent(UiEvent.Navigate(Routes.EDIT_TODO)) {
            viewModel.onEvent(TodosEvent.OnAddTodoClick)
        }
    }

    @Test
    fun clickOnDeleteTodo_shouldRemoveTodoFromRepository() = runTest {
        fakeTodoRepository.insertTodo(testTodo)
        viewModel = TodosViewModel(fakeTodoRepository, fakeNetworkRepository)
        viewModel.onEvent(TodosEvent.OnDeleteTodoClick(testTodo))
        testDispatcher.scheduler.advanceUntilIdle()
        val todos = fakeTodoRepository.getTodos().first()
        assertTrue(testTodo !in todos)
    }

    @Test
    fun markTodoAsCompleted_shouldInsertCompletedTodoAndShowCongratsSnackbar() =
        runTest {
            createViewModel()
            expectSingleUiEvent(UiEvent.ShowSnackBar("Congrats! Todo completed!")) {
                viewModel.onEvent(TodosEvent.OnDoneChange(testTodo, true))
                testDispatcher.scheduler.advanceUntilIdle()
                val todos = fakeTodoRepository.getTodos().first()
                assertTrue(todos.any { it == completedTodo })
            }
        }

    @Test
    fun markTodoAsIncomplete_shouldInsertIncompleteTodoAndShowOopsSnackbar() =
        runTest {
            createViewModel()
            expectSingleUiEvent(UiEvent.ShowSnackBar("Oops... Todo marked as incomplete ;(")) {
                viewModel.onEvent(TodosEvent.OnDoneChange(completedTodo, false))
                testDispatcher.scheduler.advanceUntilIdle()
                val todos = fakeTodoRepository.getTodos().first()
                assertTrue(todos.any { it == testTodo })
            }
        }

    private fun createViewModel() {
        viewModel = TodosViewModel(fakeTodoRepository, fakeNetworkRepository)
    }

    private suspend fun expectSingleUiEvent(expected: UiEvent, action: suspend () -> Unit) {
        viewModel.uiEvent.test {
            action()
            assertEquals(expected, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }
}