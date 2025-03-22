package com.cabral.features.splash.di

import com.cabral.core.common.domain.usecase.AutoLoginUseCase
import com.cabral.features.splash.presentation.SplashScreenViewModel
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

class SplashModuleTest : KoinTest {

    private val modules = SplashModule.modules

    private val mockAutoLoginUseCase: AutoLoginUseCase = mockk(relaxed = true)

    private val mockViewModelModules = module {
        factory { mockAutoLoginUseCase }
    }

    private val splashScreenViewModel: SplashScreenViewModel by inject()

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
    fun verifySplashScreenViewModelCall() {
        assertNotNull(splashScreenViewModel)
    }
}