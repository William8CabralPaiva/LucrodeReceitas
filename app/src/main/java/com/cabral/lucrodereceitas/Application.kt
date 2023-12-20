package com.cabral.lucrodereceitas

import android.app.Application
import com.cabral.features.di.LoginModule
import com.cabral.features.loggedin.host.di.LoggedNavigationModule
import com.cabral.features.splash.di.SplashModule
import com.cabral.host.di.NotLoggedNavigationModule
import com.cabral.hubsrc.di.HubModule
import com.cabral.ingredient.di.IngredientModule
import com.cabral.listingredients.di.ListIngredientsModule
import com.cabral.recipe.di.ListRecipeModule
import com.cabral.recipe.di.RecipeModule
import com.cabral.registeruser.di.RegisterUserModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.loadKoinModules
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.core.context.unloadKoinModules
import org.koin.core.logger.Level
import org.koin.core.module.Module
import org.koin.dsl.KoinAppDeclaration

class Application : Application() {

    private val modules by lazy {
        LoggedNavigationModule.modules +
                NotLoggedNavigationModule.modules +
                SplashModule.modules +
                LoginModule.modules +
                HubModule.modules +
                RegisterUserModule.modules +
                IngredientModule.modules +
                ListIngredientsModule.modules +
                ListRecipeModule.modules +
                RecipeModule.modules
    }

    override fun onCreate() {
        super.onCreate()
        startKoin(appDeclaration = appDeclaration())
        modules.load()
    }

    override fun onTerminate() {
        modules.unload()
        stopKoin()
        super.onTerminate()
    }

    private fun appDeclaration(): KoinAppDeclaration = {
        androidLogger(if (BuildConfig.DEBUG) Level.ERROR else Level.NONE)
        androidContext(androidContext = this@Application)
    }


    private fun List<Module>.load() {
        loadKoinModules(modules = this)
    }

    private fun List<Module>.unload() {
        unloadKoinModules(modules = this)
    }
}