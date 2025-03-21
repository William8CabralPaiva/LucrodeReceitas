import app.cash.turbine.test
import com.cabral.core.common.domain.usecase.AddIngredientUseCase
import com.cabral.ingredient.presentation.ingredient.IngredientsViewModel
import com.cabral.ingredient.presentation.ingredient.UiEvent
import com.cabral.ingredient.presentation.ingredient.UiState
import com.cabral.test_utils.stubs.ingredientStub
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class IngredientsViewModelTest {

    private lateinit var viewModel: IngredientsViewModel
    private val addIngredientUseCase: AddIngredientUseCase = mockk(relaxed = true)

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        coEvery { addIngredientUseCase(any()) } returns flowOf(Unit)
        viewModel = IngredientsViewModel(addIngredientUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `test save emits success event`() = runTest {
        // Arrange
        viewModel.uiEvent.test {
            // Act
            viewModel.save()

            // Assert
            assertEquals(UiEvent.Success, awaitItem())
        }
    }

    @Test
    fun `test save handles error`() = runTest {
        // Arrange
        coEvery { addIngredientUseCase(any()) } returns flow {
            throw Exception()
        }
        viewModel = IngredientsViewModel(addIngredientUseCase)

        viewModel.uiEvent.test {
            // Act
            viewModel.save()

            // Assert
            assertEquals(UiEvent.Error, awaitItem())
        }
    }

    @Test
    fun `test addOrEditIngredient with valid input adds ingredient`() = runTest {
        // Arrange
        viewModel.uiState.test {
            // Act
            viewModel.addOrEditIngredient("Sugar", "10", "g", "5")

            // Assert
            assertEquals(UiState.Default, awaitItem())
            assertEquals(UiState.SuccessAdd(0), awaitItem())
        }
    }

    @Test
    fun `test addOrEditIngredient with empty name triggers error`() = runTest {
        // Arrange
        viewModel.uiState.test {
            // Act
            viewModel.addOrEditIngredient("", "10", "g", "5")

            // Assert
            assertEquals(UiState.Default, awaitItem())
            assert(awaitItem() is UiState.ErrorAddEdit)
        }
    }

    @Test
    fun `test addOrEditIngredient with invalid volume triggers error`() = runTest {
        viewModel.uiState.test {
            viewModel.addOrEditIngredient("Sugar", "-5", "g", "5")
            assertEquals(UiState.Default, awaitItem())
            assert(awaitItem() is UiState.ErrorAddEdit)
        }
    }

    @Test
    fun `test addOrEditIngredient with invalid unit triggers error`() = runTest {
        viewModel.uiState.test {
            viewModel.addOrEditIngredient("Sugar", "10", "invalid", "5")
            assertEquals(UiState.Default, awaitItem())
            assert(awaitItem() is UiState.ErrorAddEdit)
        }
    }

    @Test
    fun `test addOrEditIngredient with invalid price triggers error`() = runTest {
        viewModel.uiState.test {
            viewModel.addOrEditIngredient("Sugar", "10", "g", "-5")
            assertEquals(UiState.Default, awaitItem())
            assert(awaitItem() is UiState.ErrorAddEdit)
        }
    }

    @Test
    fun `test setEditMode updates state`() = runTest {
        viewModel.uiState.test {
            viewModel.setEditMode(true, null)
            assertEquals(UiState.Default, awaitItem())
            assertEquals(UiState.EditMode(true), awaitItem())
        }
        assertTrue(viewModel.getEditMode())
    }

    @Test
    fun `test changeIngredient updates state`() = runTest {
        val ingredient = ingredientStub()
        viewModel.uiState.test {
            viewModel.changeIngredient(ingredient)
            assertEquals(UiState.Default, awaitItem())
            assertEquals(UiState.EditMode(true), awaitItem())
        }
    }

}
