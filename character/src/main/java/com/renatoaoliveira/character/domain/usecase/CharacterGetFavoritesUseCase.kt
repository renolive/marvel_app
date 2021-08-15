package com.renatoaoliveira.character.domain.usecase

import com.renatoaoliveira.character.domain.model.Character
import com.renatoaoliveira.character.domain.repository.ICharacterRepository
import kotlinx.coroutines.flow.Flow

class CharacterGetFavoritesUseCase(private val repository: ICharacterRepository) :
    ICharacterGetFavoritesUseCase {
    override fun execute(): Flow<List<Character>> =
        repository.getCharacterFavorites()
}