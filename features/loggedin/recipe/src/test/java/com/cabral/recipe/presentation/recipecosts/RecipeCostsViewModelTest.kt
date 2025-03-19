import app.cash.turbine.test
import com.cabral.core.common.domain.usecase.CostRecipeUseCase
import com.cabral.recipe.presentation.recipecosts.RecipeCostsViewModel
import com.cabral.recipe.presentation.recipecosts.UiState
import com.cabral.test_utils.stubs.recipeCostsStub
import com.cabral.test_utils.stubs.recipeStub
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class RecipeCostsViewModelTest {

    private lateinit var viewModel: RecipeCostsViewModel
    private val costRecipeUseCase: CostRecipeUseCase = mockk(relaxed = true)
    private val recipeStub = recipeStub()
    private val recipeCostStub = recipeCostsStub()

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        coEvery { costRecipeUseCase.invoke(recipeStub) } returns flowOf(recipeCostStub)
        viewModel = RecipeCostsViewModel(costRecipeUseCase).apply {
            recipe = recipeStub
        }
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `test load updates uiState correctly`() = runTest {
        // Arrange
        viewModel.uiState.test {

            // Act
            viewModel.load()

            // Assert
            assertEquals(UiState.StartLoading, awaitItem())
            val successState = awaitItem() as UiState.Success
            assertEquals(recipeCostStub.name, successState.title)
            assertEquals(UiState.StopLoading, awaitItem())
        }
    }

    @Test
    fun `test load handles error state`() = runTest {
        // Arrange
        coEvery { costRecipeUseCase.invoke(recipeStub) } returns flow {
            throw Exception("Network Error")
        }
        viewModel = RecipeCostsViewModel(costRecipeUseCase).apply {
            recipe = recipeStub
        }

        viewModel.uiState.test {
            // Act
            viewModel.load()

            // Assert
            assertEquals(UiState.StartLoading, awaitItem())
            assertEquals(UiState.Error, awaitItem())
            assertEquals(UiState.StopLoading, awaitItem())
        }
    }
}
