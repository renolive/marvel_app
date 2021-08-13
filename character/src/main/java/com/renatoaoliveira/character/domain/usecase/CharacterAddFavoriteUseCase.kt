package com.renatoaoliveira.character.domain.usecase

import com.renatoaoliveira.character.domain.model.Character
import com.renatoaoliveira.character.domain.repository.ICharacterRepository

class CharacterAddFavoriteUseCase(private val repository: ICharacterRepository) :
    ICharacterAddFavoriteUseCase {

    override suspend fun execute(character: Character) {
        repository.saveCharacterAsFavorite(character)
    }
}