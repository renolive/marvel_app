package com.renatoaoliveira.character.domain.usecase

import com.renatoaoliveira.character.domain.model.Character

interface ICharacterRemoveFavoriteUseCase {
    suspend fun execute(character: Character)
}