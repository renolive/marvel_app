package com.renatoaoliveira.character.domain.usecase

import com.renatoaoliveira.character.domain.model.Character
import kotlinx.coroutines.flow.Flow

interface ICharacterGetFavoritesUseCase {
    fun execute(): Flow<List<Character>>
}