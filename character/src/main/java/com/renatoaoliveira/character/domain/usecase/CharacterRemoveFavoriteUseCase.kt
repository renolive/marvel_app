package com.renatoaoliveira.character.domain.usecase

import com.renatoaoliveira.character.domain.model.Character
import com.renatoaoliveira.character.domain.repository.ICharacterRepository

class CharacterRemoveFavoriteUseCase(private val repository: ICharacterRepository) :
    ICharacterRemoveFavoriteUseCase {
    override suspend fun execute(character: Character) =
        repository.deleteCharacterFromFavorite(character)
}