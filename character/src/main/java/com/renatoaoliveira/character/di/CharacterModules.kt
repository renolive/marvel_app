package com.renatoaoliveira.character.di

import android.content.Context
import androidx.room.Room
import com.example.android.core.newtwork.NetworkService
import com.renatoaoliveira.character.data.repository.remote.api.CharacterServiceAPI
import com.renatoaoliveira.character.data.repository.CharacterRepository
import com.renatoaoliveira.character.data.repository.local.CharacterDatabase
import com.renatoaoliveira.character.domain.repository.ICharacterRepository
import com.renatoaoliveira.character.domain.usecase.*
import com.renatoaoliveira.character.presentation.viewmodel.CharactersListViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val characterModules = module {
    /**
     * API
     */
    single { NetworkService.createApiService(CharacterServiceAPI::class.java) }

    /**
     * Database
     */
    single { provideCharacterDatabase(androidContext()) }

    /**
     * Repository
     */
    single<ICharacterRepository> {
        CharacterRepository(
            get(),
            get<CharacterDatabase>().characterFavoriteDao
        )
    }

    /**
     * UseCase
     */
    factory<ICharacterListUseCase> { CharacterListUseCase(get()) }
    factory<ICharacterSearchUseCase> { CharacterSearchUseCase(get()) }
    factory<ICharacterAddFavoriteUseCase> { CharacterAddFavoriteUseCase(get()) }

    /**
     * ViewModel
     */
    factory { CharactersListViewModel(get(), get(), get()) }
}

private fun provideCharacterDatabase(context: Context): CharacterDatabase =
    Room.databaseBuilder(
        context.applicationContext,
        CharacterDatabase::class.java,
        "character_database"
    )
        .fallbackToDestructiveMigration()
        .build()
