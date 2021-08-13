package com.renatoaoliveira.character.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.renatoaoliveira.character.domain.model.Character
import com.renatoaoliveira.character.domain.usecase.ICharacterListUseCase
import kotlinx.coroutines.launch

class CharactersListViewModel(
    private val characterListViewModel: ICharacterListUseCase
) : ViewModel() {

    private var characterListOffset = 0

    val _characterList = MutableLiveData<CharacterListState>()
    val characterList: LiveData<CharacterListState> get() = _characterList

    fun fetchList() {
        _characterList.value = CharacterListState.Loading
        viewModelScope.launch {
            val res = characterListViewModel.execute(characterListOffset)

            _characterList.value = if (res.success) {
                CharacterListState.Success(res.data.list)
            } else {
                CharacterListState.Error
            }
        }
    }

    sealed class CharacterListState {
        object Loading : CharacterListState()
        object Error : CharacterListState()
        data class Success(val characters: List<Character>) : CharacterListState()
    }
}