package com.cabral.hubsrc.di

import com.cabral.core.common.domain.repository.IngredientRepository
import com.cabral.core.common.domain.repository.RecipeRepository
import com.cabral.core.common.domain.repository.UserRepository
import com.cabral.hubsrc.source.remote.services.IngredientServices
import com.cabral.hubsrc.source.remote.services.RecipeServices
import com.cabral.hubsrc.source.remote.services.UserServices
import com.cabral.remote.local.RemoteDataSource
import io.mockk.mockk
import kotlinx.coroutines.CoroutineDispatcher
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.verify.verifyAll

@OptIn(KoinExperimentalAPI::class)
class HubModuleTest : KoinTest {


    private val modules = HubModule.modules

    private val mockRemoteDataSource: RemoteDataSource = mockk(relaxed = true)
    private val mockUserServices: UserServices = mockk(relaxed = true)
    private val mockRecipeServices: RecipeServices = mockk(relaxed = true)
    private val mockIngredientServices: IngredientServices = mockk(relaxed = true)
    private val mockUserRepository: UserRepository = mockk(relaxed = true)
    private val mockIngredientRepository: IngredientRepository = mockk(relaxed = true)
    private val mockRecipeRepository: RecipeRepository = mockk(relaxed = true)

    private val mockViewModelModules = module {
        factory { mockRemoteDataSource }
        factory { mockUserServices }
        factory { mockRecipeServices }
        factory { mockIngredientServices }
        factory { mockUserRepository }
        factory { mockIngredientRepository }
        factory { mockRecipeRepository }
    }

    @Before
    fun setUp() {
        startKoin {
            modules(
                modules + mockViewModelModules
            )
        }
    }

    @After
    fun tearDown() {
        stopKoin()
    }

    @Test
    fun testDI_modules() {
        modules.verifyAll(
            extraTypes = listOf(CoroutineDispatcher::class)
        )
    }
}
