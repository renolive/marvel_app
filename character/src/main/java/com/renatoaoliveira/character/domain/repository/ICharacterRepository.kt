package com.renatoaoliveira.character.domain.repository

import com.renatoaoliveira.character.domain.model.Character
import com.renatoaoliveira.character.domain.model.CharacterList

interface ICharacterRepository {
    suspend fun saveCharacterAsFavorite(character: Character)

    suspend fun getCharactersList(offset: Int): CharacterResult<CharacterList>

    suspend fun searchCharacters(offset: Int, query: String): CharacterResult<CharacterList>
}