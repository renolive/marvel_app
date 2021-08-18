package com.renatoaoliveira.character.domain.usecase

import com.renatoaoliveira.character.domain.model.CharacterList
import com.renatoaoliveira.character.domain.repository.CharacterResult
import com.renatoaoliveira.character.domain.repository.ICharacterRepository

class CharacterListUseCase(
    private val repository: ICharacterRepository,
) : ICharacterListUseCase {

    override suspend fun execute(offset: Int, query: String): CharacterResult<CharacterList> =
        try {
            repository.getCharactersList(offset, query)
        } catch (e: Exception) {
            CharacterResult(
                data = CHARACTER_LIST_ON_ERROR,
                statusCode = STATUS_CODE_ON_ERROR,
                message = e.message.orEmpty(),
                false
            )
        }

    companion object {
        val CHARACTER_LIST_ON_ERROR = CharacterList(-1, -1, -1, emptyList())
        val STATUS_CODE_ON_ERROR = 0
    }
}