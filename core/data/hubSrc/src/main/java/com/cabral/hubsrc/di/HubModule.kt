package com.cabral.hubsrc.di

import com.cabral.core.common.domain.repository.IngredientRepository
import com.cabral.core.common.domain.repository.RecipeRepository
import com.cabral.core.common.domain.repository.UserRepository
import com.cabral.core.common.domain.usecase.AddIngredientUseCase
import com.cabral.core.common.domain.usecase.AddRecipeUseCase
import com.cabral.core.common.domain.usecase.AddUserUseCase
import com.cabral.core.common.domain.usecase.AutoLoginUseCase
import com.cabral.core.common.domain.usecase.CostRecipeUseCase
import com.cabral.core.common.domain.usecase.ForgotPasswordUseCase
import com.cabral.core.common.domain.usecase.GoogleLoginUseCase
import com.cabral.core.common.domain.usecase.ListIngredientUseCase
import com.cabral.core.common.domain.usecase.LoginUseCase
import com.cabral.core.common.domain.usecase.DeleteIngredientUseCase
import com.cabral.core.common.domain.usecase.DeleteRecipeUseCase
import com.cabral.core.common.domain.usecase.GetListRecipeUseCase
import com.cabral.hubsrc.repository.IngredientRepositoryImpl
import com.cabral.hubsrc.repository.RecipeRepositoryImpl
import com.cabral.hubsrc.repository.UserRepositoryImpl
import com.cabral.hubsrc.source.remote.RemoteDataSourceImpl
import com.cabral.remote.local.RemoteDataSource
import org.koin.core.module.Module
import org.koin.dsl.module

object HubModule {

    val modules get() = listOf(loginModules)

    private val loginModules: Module = module {
        factory<RemoteDataSource> { RemoteDataSourceImpl() }
        factory<UserRepository> { UserRepositoryImpl(get()) }
        factory<IngredientRepository> { IngredientRepositoryImpl(get()) }
        factory<RecipeRepository> { RecipeRepositoryImpl(get()) }
        factory { AddUserUseCase(get()) }
        factory { LoginUseCase(get()) }
        factory { AutoLoginUseCase(get()) }
        factory { GoogleLoginUseCase(get()) }
        factory { ForgotPasswordUseCase(get()) }
        factory { GetListRecipeUseCase(get()) }
        factory { DeleteRecipeUseCase(get()) }


        //TODO VER
        factory { AddIngredientUseCase(get()) }
        factory { AddRecipeUseCase(get()) }
        factory { DeleteIngredientUseCase(get()) }
        factory { ListIngredientUseCase(get()) }
        factory { CostRecipeUseCase(get()) }


    }
}
