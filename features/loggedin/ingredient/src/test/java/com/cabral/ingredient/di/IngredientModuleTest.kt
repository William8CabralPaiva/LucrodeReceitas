package com.cabral.ingredient.di

import com.cabral.core.common.domain.usecase.AddIngredientUseCase
import com.cabral.ingredient.presentation.ingredient.IngredientsViewModel
import io.mockk.mockk
import kotlinx.coroutines.CoroutineDispatcher
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.inject
import kotlin.test.assertNotNull

class IngredientModuleTest : KoinTest {

    private val modules = IngredientModule.modules

    private val mockAddIngredientUseCase: AddIngredientUseCase = mockk(relaxed = true)

    private val mockViewModelModules = module {
        factory { mockAddIngredientUseCase }
    }

    private val ingredientsViewModel: IngredientsViewModel by inject()

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
    fun verify_ingredientsViewModel_call() {
        assertNotNull(ingredientsViewModel)
    }
}
