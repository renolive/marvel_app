package com.renatoaoliveira.marvel.application

import android.app.Application
import com.example.android.core.newtwork.NetworkService
import com.renatoaoliveira.marvel.BuildConfig

class MainApplication: Application() {
    override fun onCreate() {
        super.onCreate()

        NetworkService.configure(BuildConfig.BASE_URL)
    }
}