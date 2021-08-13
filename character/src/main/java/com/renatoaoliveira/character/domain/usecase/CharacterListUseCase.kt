package com.renatoaoliveira.character.domain.usecase

import com.renatoaoliveira.character.domain.model.Character
import com.renatoaoliveira.character.domain.model.CharacterList
import com.renatoaoliveira.character.domain.repository.CharacterResult
import com.renatoaoliveira.character.domain.repository.ICharacterRepository

class CharacterListUseCase(
    private val repository: ICharacterRepository
) : ICharacterListUseCase {

    override suspend fun execute(offset: Int): CharacterResult<CharacterList> =
        repository.getCharactersList(offset)
}