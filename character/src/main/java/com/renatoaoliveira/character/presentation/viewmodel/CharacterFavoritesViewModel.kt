package com.renatoaoliveira.character.presentation.viewmodel

import androidx.lifecycle.*
import com.renatoaoliveira.character.domain.model.Character
import com.renatoaoliveira.character.domain.usecase.ICharacterGetFavoritesUseCase
import com.renatoaoliveira.character.domain.usecase.ICharacterRemoveFavoriteUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlin.coroutines.CoroutineContext

class CharacterFavoritesViewModel(
    private val characterRemoveFavoriteUseCase: ICharacterRemoveFavoriteUseCase,
    private val characterGetFavoritesUseCase: ICharacterGetFavoritesUseCase,
    private val dispatcher: CoroutineContext
) : ViewModel() {

    val characterFavorites: LiveData<CharacterFavoriteState> =
        characterGetFavoritesUseCase.execute()
            .flowOn(dispatcher)
            .onStart {
                CharacterFavoriteState.Loading
            }
            .catch {
                CharacterFavoriteState.Error
            }
            .transform<List<Character>, CharacterFavoriteState> {
                CharacterFavoriteState.Success(it)
            }
            .asLiveData()

    sealed class CharacterFavoriteState {
        object Idle : CharacterFavoriteState()
        object Loading : CharacterFavoriteState()
        object Error : CharacterFavoriteState()
        class Success(val list: List<Character>) : CharacterFavoriteState()
    }
}