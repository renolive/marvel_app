package com.renatoaoliveira.character.di

import com.example.android.core.newtwork.NetworkService
import com.example.android.core.newtwork.provideDatabase
import com.renatoaoliveira.character.data.repository.CharacterRepository
import com.renatoaoliveira.character.data.repository.local.CharacterDatabase
import com.renatoaoliveira.character.data.repository.remote.api.CharacterServiceAPI
import com.renatoaoliveira.character.domain.repository.ICharacterRepository
import com.renatoaoliveira.character.domain.usecase.*
import com.renatoaoliveira.character.presentation.viewmodel.CharacterFavoritesViewModel
import com.renatoaoliveira.character.presentation.viewmodel.CharactersListViewModel
import kotlinx.coroutines.Dispatchers
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
    single { provideDatabase<CharacterDatabase>(androidContext(), "character_database") }

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
    factory<ICharacterRemoveFavoriteUseCase> { CharacterRemoveFavoriteUseCase(get()) }
    factory<ICharacterGetFavoritesUseCase> { CharacterGetFavoritesUseCase(get()) }

    /**
     * ViewModel
     */
    factory { CharactersListViewModel(get(), get(), get(), Dispatchers.Main) }
    factory { CharacterFavoritesViewModel(get(), get(), Dispatchers.Main) }
}