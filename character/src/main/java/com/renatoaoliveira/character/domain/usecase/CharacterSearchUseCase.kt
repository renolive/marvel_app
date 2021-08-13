package com.renatoaoliveira.character.domain.usecase

import com.renatoaoliveira.character.domain.model.CharacterList
import com.renatoaoliveira.character.domain.repository.CharacterResult
import com.renatoaoliveira.character.domain.repository.ICharacterRepository

class CharacterSearchUseCase(private val repository: ICharacterRepository) :
    ICharacterSearchUseCase {

    override suspend fun execute(offset: Int, query: String): CharacterResult<CharacterList> =
        try {
            repository.searchCharacters(offset, query)
        } catch (e: Exception) {
            CharacterResult(
                data = CharacterList(-1, -1, -1, emptyList()),
                statusCode = 0,
                message = e.message.orEmpty(),
                false
            )
        }
}