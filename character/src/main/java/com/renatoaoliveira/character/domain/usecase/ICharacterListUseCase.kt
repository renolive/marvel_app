package com.renatoaoliveira.character.domain.usecase

import com.renatoaoliveira.character.domain.model.CharacterList
import com.renatoaoliveira.character.domain.repository.CharacterResult

interface ICharacterListUseCase {
    suspend fun execute(offset: Int, query: String): CharacterResult<CharacterList>
}