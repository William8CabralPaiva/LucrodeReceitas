import com.cabral.core.common.domain.model.User
import com.cabral.hubsrc.source.remote.RemoteDataSourceImpl
import com.cabral.hubsrc.source.remote.services.IngredientServices
import com.cabral.hubsrc.source.remote.services.RecipeServices
import com.cabral.hubsrc.source.remote.services.UserServices
import com.cabral.test_utils.stubs.ingredientListStub
import com.cabral.test_utils.stubs.ingredientStub
import com.cabral.test_utils.stubs.recipeProfitPriceListStub
import com.cabral.test_utils.stubs.recipeStub
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.get

@ExperimentalCoroutinesApi
class RemoteDataSourceTest : KoinTest {

    private lateinit var remoteDataSourceImpl: RemoteDataSourceImpl
    private lateinit var userService: UserServices
    private lateinit var recipeService: RecipeServices
    private lateinit var ingredientService: IngredientServices

    @Before
    fun setup() {
        startKoin {
            modules(
                module {
                    single { mockk<UserServices>(relaxed = true) }
                    single { mockk<RecipeServices>(relaxed = true) }
                    single { mockk<IngredientServices>(relaxed = true) }
                }
            )
        }

        userService = get()
        recipeService = get()
        ingredientService = get()

        remoteDataSourceImpl = RemoteDataSourceImpl(
            userService = userService,
            recipeService = recipeService,
            ingredientService = ingredientService
        )
    }

    @Test
    fun `test addUser calls userService addUser`() = runTest {
        // Arrange
        val user = User()
        coEvery { userService.addUser(user) } returns flowOf(Unit)

        // Act
        remoteDataSourceImpl.addUser(user).toList()

        // Assert
        coVerify { userService.addUser(user) }
    }

    @Test
    fun `test login calls userService login`() = runTest {
        // Arrange
        val user = User()
        coEvery { userService.login(user) } returns flowOf(user)

        // Act
        val result = remoteDataSourceImpl.login(user).toList()

        // Assert
        coVerify { userService.login(user) }
        assert(result == listOf(user))
    }

    @Test
    fun `test autoLogin calls userService autoLogin`() = runTest {
        // Arrange
        val key = "testKey"
        val user = User()
        coEvery { userService.autoLogin(key) } returns flowOf(user)

        // Act
        val result = remoteDataSourceImpl.autoLogin(key).toList()

        // Assert
        coVerify { userService.autoLogin(key) }
        assert(result == listOf(user))
    }

    @Test
    fun `test googleLogin calls userService googleLogin`() = runTest {
        // Arrange
        val email = "test@test.com"
        val name = "Test User"
        val user = User()
        coEvery { userService.googleLogin(email, name) } returns flowOf(user)

        // Act
        val result = remoteDataSourceImpl.googleLogin(email, name).toList()

        // Assert
        coVerify { userService.googleLogin(email, name) }
        assert(result == listOf(user))
    }

    @Test
    fun `test forgotPassword calls userService forgotPassword`() = runTest {
        // Arrange
        val email = "test@test.com"
        coEvery { userService.forgotPassword(email) } returns Unit

        // Act
        remoteDataSourceImpl.forgotPassword(email)

        // Assert
        coVerify { userService.forgotPassword(email) }
    }

    @Test
    fun `test getAllRecipes calls recipeService getAllRecipes`() = runTest {
        // Arrange
        val recipeList = recipeProfitPriceListStub()
        coEvery { recipeService.getAllRecipes() } returns flowOf(recipeList)

        // Act
        val result = remoteDataSourceImpl.getAllRecipes().toList()

        // Assert
        coVerify { recipeService.getAllRecipes() }
        assert(result == listOf(recipeList))
    }

    @Test
    fun `test addRecipe calls recipeService addRecipe`() = runTest {
        // Arrange
        val recipe = recipeStub()
        coEvery { recipeService.addRecipe(recipe) } returns flowOf("success")

        // Act
        val result = remoteDataSourceImpl.addRecipe(recipe).toList()

        // Assert
        coVerify { recipeService.addRecipe(recipe) }
        assert(result == listOf("success"))
    }

    @Test
    fun `test deleteRecipe calls recipeService deleteRecipe`() = runTest {
        // Arrange
        val keyDocument = "testKey"
        coEvery { recipeService.deleteRecipe(keyDocument) } returns flowOf(Unit)

        // Act
        remoteDataSourceImpl.deleteRecipe(keyDocument).toList()

        // Assert
        coVerify { recipeService.deleteRecipe(keyDocument) }
    }

    @Test
    fun `test getAllIngredients calls ingredientService getAllIngredients`() = runTest {
        // Arrange
        val ingredientList = ingredientListStub()
        coEvery { ingredientService.getAllIngredients() } returns flowOf(ingredientList)

        // Act
        val result = remoteDataSourceImpl.getAllIngredients().toList()

        // Assert
        coVerify { ingredientService.getAllIngredients() }
        assert(result == listOf(ingredientList))
    }

    @Test
    fun `test addIngredient calls ingredientService addIngredient`() = runTest {
        // Arrange
        val ingredientList = ingredientListStub()
        coEvery { ingredientService.addIngredient(ingredientList) } returns flowOf(Unit)

        // Act
        remoteDataSourceImpl.addIngredient(ingredientList).toList()

        // Assert
        coVerify { ingredientService.addIngredient(ingredientList) }
    }

    @Test
    fun `test deleteIngredient calls ingredientService deleteIngredient`() = runTest {
        // Arrange
        val ingredient = ingredientStub()
        coEvery { ingredientService.deleteIngredient(ingredient) } returns flowOf(Unit)

        // Act
        remoteDataSourceImpl.deleteIngredient(ingredient).toList()

        // Assert
        coVerify { ingredientService.deleteIngredient(ingredient) }
    }

    @After
    fun tearDown() {
        stopKoin()
    }
}
