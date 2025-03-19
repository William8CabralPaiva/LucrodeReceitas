package com.cabral.ingredient.di

import com.cabral.core.common.domain.usecase.DeleteIngredientUseCase
import com.cabral.core.common.domain.usecase.ListIngredientUseCase
import com.cabral.ingredient.presentation.listingredient.ListIngredientsViewModel
import io.mockk.mockk
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.inject
import kotlin.test.assertNotNull

class ListIngredientsModuleTest : KoinTest {

    private val modules = ListIngredientsModule.modules

    private val mockListIngredientsUseCase: ListIngredientUseCase = mockk(relaxed = true)
    private val mockDeleteIngredientUseCase: DeleteIngredientUseCase = mockk(relaxed = true)

    private val mockViewModelModules = module {
        factory { mockListIngredientsUseCase }
        factory { mockDeleteIngredientUseCase }
    }

    private val listIngredientsViewModel: ListIngredientsViewModel by inject()

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
    fun verify_listIngredientsViewModel_call() {
        assertNotNull(listIngredientsViewModel)
    }
}
