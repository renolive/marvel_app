package com.renatoaoliveira.character.di

import com.example.android.core.newtwork.NetworkService
import com.renatoaoliveira.character.data.api.CharactersServiceAPI
import com.renatoaoliveira.character.data.repository.CharacterRepository
import com.renatoaoliveira.character.domain.repository.ICharacterRepository
import com.renatoaoliveira.character.domain.usecase.CharacterListUseCase
import com.renatoaoliveira.character.domain.usecase.ICharacterListUseCase
import com.renatoaoliveira.character.presentation.viewmodel.CharactersListViewModel
import org.koin.dsl.module

val characterModules = module {
    /**
     * APIs
     */
    single { NetworkService.createApiService(CharactersServiceAPI::class.java) }
    single<ICharacterRepository> { CharacterRepository(get()) }
    factory<ICharacterListUseCase> { CharacterListUseCase(get()) }
    factory { CharactersListViewModel(get()) }
}