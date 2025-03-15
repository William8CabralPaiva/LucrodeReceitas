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
import com.cabral.hubsrc.source.remote.services.IngredientServices
import com.cabral.hubsrc.source.remote.services.RecipeServices
import com.cabral.hubsrc.source.remote.RemoteDataSourceImpl
import com.cabral.hubsrc.source.remote.services.IngredientServicesImpl
import com.cabral.hubsrc.source.remote.services.RecipeServicesImpl
import com.cabral.hubsrc.source.remote.services.UserServices
import com.cabral.hubsrc.source.remote.services.UserServicesImpl
import com.cabral.remote.local.RemoteDataSource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import org.koin.core.module.Module
import org.koin.dsl.module


object HubModule {

    val modules get() = listOf(loginModules)

    private val loginModules: Module = module {

        single<RemoteDataSource> { RemoteDataSourceImpl(get(), get(), get()) }
        single<UserServices> {
            UserServicesImpl(
                FirebaseFirestore.getInstance(),
                FirebaseAuth.getInstance()
            )
        }
        single<RecipeServices> { RecipeServicesImpl(FirebaseFirestore.getInstance()) }
        single<IngredientServices> { IngredientServicesImpl(FirebaseFirestore.getInstance()) }


        factory<UserRepository> { UserRepositoryImpl(get()) }
        factory<IngredientRepository> { IngredientRepositoryImpl(get()) }
        factory<RecipeRepository> { RecipeRepositoryImpl(get()) }

        factory<AddUserUseCase> { AddUserUseCase(get()) }
        factory<LoginUseCase> { LoginUseCase(get()) }
        factory<AutoLoginUseCase> { AutoLoginUseCase(get()) }
        factory<GoogleLoginUseCase> { GoogleLoginUseCase(get()) }
        factory<ForgotPasswordUseCase> { ForgotPasswordUseCase(get()) }
        factory<GetListRecipeUseCase> { GetListRecipeUseCase(get()) }
        factory<DeleteRecipeUseCase> { DeleteRecipeUseCase(get()) }

        factory<AddIngredientUseCase> { AddIngredientUseCase(get()) }
        factory<AddRecipeUseCase> { AddRecipeUseCase(get()) }
        factory<DeleteIngredientUseCase> { DeleteIngredientUseCase(get()) }
        factory<ListIngredientUseCase> { ListIngredientUseCase(get()) }
        factory<CostRecipeUseCase> { CostRecipeUseCase(get()) }
    }
}
