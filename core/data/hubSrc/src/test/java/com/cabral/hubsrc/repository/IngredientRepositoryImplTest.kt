import app.cash.turbine.test
import com.cabral.hubsrc.repository.IngredientRepositoryImpl
import com.cabral.remote.local.RemoteDataSource
import com.cabral.test_utils.stubs.ingredientListStub
import com.cabral.test_utils.stubs.ingredientStub
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class IngredientRepositoryImplTest {

    private lateinit var repository: IngredientRepositoryImpl
    private val remoteDataSource: RemoteDataSource = mockk()

    @Before
    fun setUp() {
        repository = IngredientRepositoryImpl(remoteDataSource)
    }

    @Test
    fun `getAllIngredients should return data from remoteDataSource`() = runTest {
        val ingredients = ingredientListStub()
        coEvery { remoteDataSource.getAllIngredients() } returns flowOf(ingredients)

        repository.getAllIngredients().test {
            assertEquals(ingredients, awaitItem())
            awaitComplete()
        }
    }

    @Test
    fun `addIngredient should call remoteDataSource addIngredient`() = runTest {
        val ingredients = ingredientListStub()
        coEvery { remoteDataSource.addIngredient(ingredients) } returns flowOf(Unit)

        repository.addIngredient(ingredients).test {
            awaitItem()
            awaitComplete()
        }

        coVerify { remoteDataSource.addIngredient(ingredients) }
    }

    @Test
    fun `deleteIngredient should call remoteDataSource deleteIngredient`() = runTest {
        val ingredient = ingredientStub()
        coEvery { remoteDataSource.deleteIngredient(ingredient) } returns flowOf(Unit)

        repository.deleteIngredient(ingredient).test {
            awaitItem()
            awaitComplete()
        }

        coVerify { remoteDataSource.deleteIngredient(ingredient) }
    }
}
