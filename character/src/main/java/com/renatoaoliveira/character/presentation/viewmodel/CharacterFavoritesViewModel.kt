package com.renatoaoliveira.character.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.renatoaoliveira.character.domain.model.Character
import com.renatoaoliveira.character.domain.usecase.ICharacterAddFavoriteUseCase
import com.renatoaoliveira.character.domain.usecase.ICharacterGetFavoritesUseCase
import com.renatoaoliveira.character.domain.usecase.ICharacterRemoveFavoriteUseCase
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import timber.log.Timber
import kotlin.coroutines.CoroutineContext

open class CharacterFavoritesViewModel(
    private val characterRemoveFavoriteUseCase: ICharacterRemoveFavoriteUseCase,
    private val characterAddFavoriteUseCase: ICharacterAddFavoriteUseCase,
    private val characterGetFavoritesUseCase: ICharacterGetFavoritesUseCase,
    private val dispatcher: CoroutineContext
) : ViewModel() {

    private val _characterFavorites =
        MutableLiveData<CharacterFavoriteState>(CharacterFavoriteState.Idle)
    val characterFavorites: LiveData<CharacterFavoriteState> get() = _characterFavorites

    init {
        viewModelScope.launch {
            characterGetFavoritesUseCase.execute()
                .flowOn(dispatcher)
                .onStart {
                    CharacterFavoriteState.Loading
                }
                .catch {
                    CharacterFavoriteState.Error
                }
                .collectLatest {
                    _characterFavorites.value = CharacterFavoriteState.Success(it)
                }
        }
    }

    fun onFavoriteClick(character: Character, shouldMarkAsFavorite: Boolean) {
        viewModelScope.launch(dispatcher) {
            try {
                if (shouldMarkAsFavorite) {
                    characterAddFavoriteUseCase.execute(character)
                } else {
                    characterRemoveFavoriteUseCase.execute(character)
                }
            } catch (e: Exception) {
                Timber.e(e, "Um erro ocorreu ao favoritar personagem")
            }
        }
    }

    sealed class CharacterFavoriteState {
        object Idle : CharacterFavoriteState()
        object Loading : CharacterFavoriteState()
        object Error : CharacterFavoriteState()
        class Success(val list: List<Character>) : CharacterFavoriteState()
    }
}