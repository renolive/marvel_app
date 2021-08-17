package com.renatoaoliveira.character.domain.repository

import com.renatoaoliveira.character.domain.model.Character
import com.renatoaoliveira.character.domain.model.CharacterList
import kotlinx.coroutines.flow.Flow

interface ICharacterRepository {
    suspend fun saveCharacterAsFavorite(character: Character)

    suspend fun deleteCharacterFromFavorite(character: Character)

    fun getCharacterFavorites(): Flow<List<Character>>

    suspend fun getCharactersList(offset: Int, query: String): CharacterResult<CharacterList>
}