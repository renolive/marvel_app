package com.renatoaoliveira.character.presentation.viewmodel

import androidx.lifecycle.*
import com.renatoaoliveira.character.domain.model.Character
import com.renatoaoliveira.character.domain.usecase.*
import com.renatoaoliveira.character.presentation.mapper.mapToVO
import com.renatoaoliveira.character.presentation.model.CharacterVO
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import timber.log.Timber
import kotlin.coroutines.CoroutineContext

class CharactersListViewModel(
    private val characterListUseCase: ICharacterListUseCase,
    private val characterSearchUseCase: ICharacterSearchUseCase,
    private val characterAddFavoriteUseCase: ICharacterAddFavoriteUseCase,
    private val characterRemoveFavoriteUseCase: ICharacterRemoveFavoriteUseCase,
    private val characterGetFavoritesUseCase: ICharacterGetFavoritesUseCase,
    private val dispatcher: CoroutineContext
) : ViewModel() {

    private var characterListOffset = 0

    private val _characterList = MutableLiveData<CharacterListState>()
    val characterList: LiveData<CharacterListState> get() = _characterList

    private val _characterSearchList = MutableLiveData<CharacterSearchListState>()
    val characterSearchList: LiveData<CharacterSearchListState> get() = _characterSearchList

    private val _characterFavorites =
        MutableLiveData<CharacterFavoriteState>(
            CharacterFavoriteState.Idle
        )
    val characterFavorites: LiveData<CharacterFavoriteState> get() = _characterFavorites

    private val _charactersLiveDataMerger = MediatorLiveData<List<CharacterVO>>()
    val charactersLiveDataMerger: LiveData<List<CharacterVO>> get() = _charactersLiveDataMerger

    init {
        viewModelScope.launch {
            characterGetFavoritesUseCase.execute()
                .flowOn(dispatcher)
                .onStart {
                    CharacterFavoritesViewModel.CharacterFavoriteState.Loading
                }
                .catch {
                    CharacterFavoritesViewModel.CharacterFavoriteState.Error
                }
                .collectLatest {
                    _characterFavorites.value =
                        CharacterFavoriteState.Success(it)
                }
        }

        _charactersLiveDataMerger.addSource(_characterList) {
            if (it is CharacterListState.Success) {
                val favoriteList =
                    (_characterFavorites.value as? CharacterFavoriteState.Success)?.list
                        ?: emptyList()
                _charactersLiveDataMerger.value = updateCharacterVOList(it.characters, favoriteList)
            }
        }
        _charactersLiveDataMerger.addSource(_characterFavorites) {
            if (it is CharacterFavoriteState.Success) {
                val characterList =
                    (_characterList.value as? CharacterListState.Success)?.characters
                        ?: return@addSource
                _charactersLiveDataMerger.value = updateCharacterVOList(characterList, it.list)
            }
        }
    }

    private fun updateCharacterVOList(
        characters: List<Character>,
        favorites: List<Character>
    ): List<CharacterVO> {
        val favoritesId = favorites.map { it.id }
        return characters.map { it.mapToVO(it.id in favoritesId) }
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

    fun fetchList(isFirstPage: Boolean = true) {
        _characterList.value = CharacterListState.Loading

        viewModelScope.launch(dispatcher) {
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

//    fun searchCharacter(query: String, isFirstPage: Boolean = true) {
//        _characterSearchList.value = CharacterSearchListState.Loading
//        viewModelScope.launch(dispatcher) {
//            if (isFirstPage) characterListOffset = 0
//            val res = characterSearchUseCase.execute(characterListOffset, query)
//
//            _characterSearchList.value = if (res.success) {
//                characterListOffset += res.data.count
//                CharacterSearchListState.Success(res.data.list)
//            } else {
//                CharacterSearchListState.Error
//            }
//
//            println("### Search " + res.data.count + res.data.list)
//        }
//    }

    fun resetCharacterList() {
        _characterList.value = CharacterListState.Idle
    }

    fun resetCharacterSearchList() {
        _characterSearchList.value = CharacterSearchListState.Idle
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
        object Loading : CharacterFavoriteState()
        object Error : CharacterFavoriteState()
        class Success(val list: List<Character>) : CharacterFavoriteState()
    }
}