package com.cabral.hubsrc.di

import com.cabral.core.common.domain.repository.LucroReceitaRepository
import com.cabral.core.common.domain.usecase.AddUserUseCase
import com.cabral.core.common.domain.usecase.AutoLoginUseCase
import com.cabral.core.common.domain.usecase.GoogleLoginUseCase
import com.cabral.core.common.domain.usecase.LoginUseCase
import com.cabral.hubsrc.repository.LucroReceitaRepositoryImpl
import com.cabral.hubsrc.source.remote.RemoteDataSourceImpl
import com.cabral.remote.local.RemoteDataSource
import org.koin.core.module.Module
import org.koin.dsl.module

object HubModules {

    val modules get() = listOf(loginModules)

    private val loginModules: Module = module {
        factory<RemoteDataSource> { RemoteDataSourceImpl() }
        factory<LucroReceitaRepository> { LucroReceitaRepositoryImpl(get()) }
        factory { AddUserUseCase(get()) }
        factory { LoginUseCase(get()) }
        factory { AutoLoginUseCase(get()) }
        factory { GoogleLoginUseCase(get()) }
    }
}
