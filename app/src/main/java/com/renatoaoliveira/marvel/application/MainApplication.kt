package com.renatoaoliveira.marvel.application

import android.app.Application
import com.example.android.core.newtwork.NetworkService
import com.renatoaoliveira.character.di.characterModules
import com.renatoaoliveira.marvel.BuildConfig
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MainApplication: Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@MainApplication)
            modules(
                listOf(
                    characterModules
                )
            )
        }

    }
}