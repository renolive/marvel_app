package com.renatoaoliveira.character.domain.repository

import com.renatoaoliveira.character.domain.model.CharacterList

interface ICharacterRepository {
    suspend fun getCharactersList(offset: Int): CharacterResult<CharacterList>
}