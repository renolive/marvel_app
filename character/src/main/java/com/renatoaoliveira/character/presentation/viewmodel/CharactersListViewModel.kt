package com.renatoaoliveira.character.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.renatoaoliveira.character.domain.model.Character
import com.renatoaoliveira.character.domain.usecase.ICharacterAddFavoriteUseCase
import com.renatoaoliveira.character.domain.usecase.ICharacterListUseCase
import com.renatoaoliveira.character.domain.usecase.ICharacterSearchUseCase
import kotlinx.coroutines.launch
import timber.log.Timber

class CharactersListViewModel(
    private val characterListUseCase: ICharacterListUseCase,
    private val characterSearchUseCase: ICharacterSearchUseCase,
    private val characterAddFavoriteUseCase: ICharacterAddFavoriteUseCase
) : ViewModel() {

    private var characterListOffset = 0

    private val _characterList = MutableLiveData<CharacterListState>()
    val characterList: LiveData<CharacterListState> get() = _characterList

    private val _characterSearchList = MutableLiveData<CharacterSearchListState>()
    val characterSearchList: LiveData<CharacterSearchListState> get() = _characterSearchList

    private val _characterFavorite = MutableLiveData<CharacterFavoriteState>()
    val characterFavorite: LiveData<CharacterFavoriteState> get() = _characterFavorite

    fun fetchList(isFirstPage: Boolean = true) {
        _characterList.value = CharacterListState.Loading
        viewModelScope.launch {
            if (isFirstPage) characterListOffset = 0

            val res = characterListUseCase.execute(characterListOffset)

            _characterList.value = if (res.success) {
                characterListOffset += res.data.count
                CharacterListState.Success(res.data.list)
            } else {
                CharacterListState.Error
            }

            println("### List " + res.data.list)
        }
    }

    fun searchCharacter(query: String, isFirstPage: Boolean = true) {
        _characterSearchList.value = CharacterSearchListState.Loading
        viewModelScope.launch {
            if (isFirstPage) characterListOffset = 0
            val res = characterSearchUseCase.execute(characterListOffset, query)

            _characterSearchList.value = if (res.success) {
                characterListOffset += res.data.count
                CharacterSearchListState.Success(res.data.list)
            } else {
                CharacterSearchListState.Error
            }

            println("### Search " + res.data.count + res.data.list)
        }
    }

    fun favoriteCharacter(character: Character) {
        viewModelScope.launch {
            try {
                characterAddFavoriteUseCase.execute(character)
            } catch (e: Exception) {
                Timber.e(e, "Um erro ocorreu ao favoritar personagem")
                _characterFavorite.value = CharacterFavoriteState.Error
            }
        }
    }

    fun resetCharacterList() {
        _characterList.value = CharacterListState.Idle
    }

    fun resetCharacterSearchList() {
        _characterSearchList.value = CharacterSearchListState.Idle
    }

    fun resetCharacterFavorite() {
        _characterFavorite.value = CharacterFavoriteState.Idle
    }

    sealed class CharacterListState {
        object Loading : CharacterListState()
        object Idle : CharacterListState()
        object Error : CharacterListState()
        data class Success(val characters: List<Character>) : CharacterListState()
    }

    sealed class CharacterSearchListState {
        object Loading : CharacterSearchListState()
        object Idle : CharacterSearchListState()
        object Error : CharacterSearchListState()
        data class Success(val characters: List<Character>) : CharacterSearchListState()
    }

    sealed class CharacterFavoriteState {
        object Idle : CharacterFavoriteState()
        object Error : CharacterFavoriteState()
    }
}