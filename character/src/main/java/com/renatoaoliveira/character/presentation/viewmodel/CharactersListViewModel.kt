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
    private val characterAddFavoriteUseCase: ICharacterAddFavoriteUseCase,
    private val characterRemoveFavoriteUseCase: ICharacterRemoveFavoriteUseCase,
    private val characterGetFavoritesUseCase: ICharacterGetFavoritesUseCase,
    private val dispatcher: CoroutineContext
) : ViewModel() {

    private var characterListOffset = 0
    private var query = ""
    private var totalItems = Int.MAX_VALUE

    var isFirstPage = true
        private set

    private val _characterList = MutableLiveData<CharacterListState>()
    val characterList: LiveData<CharacterListState> get() = _characterList

    private val _characterFavorites =
        MutableLiveData<CharacterFavoriteState>(
            CharacterFavoriteState.Idle
        )

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
                val currentList =
                    if (isFirstPage) emptyList() else _charactersLiveDataMerger.value
                        ?: emptyList()
                _charactersLiveDataMerger.value =
                    currentList + getCharacterVOList(it.characters, favoriteList)
            }
        }
        _charactersLiveDataMerger.addSource(_characterFavorites) {
            if (it is CharacterFavoriteState.Success) {
                val currentList = _charactersLiveDataMerger.value ?: return@addSource
                _charactersLiveDataMerger.value = updateCharacterVOList(currentList, it.list)
            }
        }
    }

    private fun getCharacterVOList(
        characters: List<Character>,
        favorites: List<Character>
    ): List<CharacterVO> {
        val favoritesId = favorites.map { it.id }
        return characters.map { it.mapToVO(it.id in favoritesId) }
    }

    private fun updateCharacterVOList(
        characters: List<CharacterVO>,
        favorites: List<Character>
    ): List<CharacterVO> {
        val favoritesId = favorites.map { it.id }
        return characters.map { it.apply { isFavorite = it.id in favoritesId } }
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

    fun fetchFirstCharacters(query: String) {
        characterListOffset = 0
        totalItems = Int.MAX_VALUE
        this.query = query
        fetchCharacters()
    }

    fun fetchNextCharacters() {
        if (characterListOffset < totalItems) {
            fetchCharacters()
        }
    }

    private fun fetchCharacters() {
        isFirstPage = characterListOffset == 0

        _characterList.value = CharacterListState.Loading

        viewModelScope.launch(dispatcher) {

            val res = characterListUseCase.execute(characterListOffset, query)

            _characterList.value = if (res.success) {
                characterListOffset += res.data.count
                totalItems = res.data.total
                CharacterListState.Success(res.data.list)
            } else {
                CharacterListState.Error
            }

            //TODO remover
            println("### List " + res.data.list)
        }
    }

    sealed class CharacterListState {
        object Loading : CharacterListState()
        object Error : CharacterListState()
        data class Success(val characters: List<Character>) : CharacterListState()
    }

    sealed class CharacterFavoriteState {
        object Idle : CharacterFavoriteState()
        class Success(val list: List<Character>) : CharacterFavoriteState()
    }
}