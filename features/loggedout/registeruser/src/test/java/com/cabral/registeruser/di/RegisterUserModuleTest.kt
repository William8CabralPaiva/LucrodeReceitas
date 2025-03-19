package com.cabral.registeruser.di

import com.cabral.core.common.domain.usecase.AddUserUseCase
import com.cabral.registeruser.presentation.RegisterUserViewModel
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

class RegisterUserModuleTest : KoinTest {

    private val modules = RegisterUserModule.modules

    private val mockAddUserUseCase: AddUserUseCase = mockk(relaxed = true)

    private val mockViewModelModules = module {
        factory { mockAddUserUseCase }
    }

    private val registerUserViewModel: RegisterUserViewModel by inject()

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
    fun verifyRegisterUserViewModelCall() {
        assertNotNull(registerUserViewModel)
    }
}