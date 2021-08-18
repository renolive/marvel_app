package com.renatoaoliveira.character.domain.usecase

import com.renatoaoliveira.character.domain.usecase.CharacterListUseCase.Companion.CHARACTER_LIST_ON_ERROR
import com.renatoaoliveira.character.domain.usecase.CharacterListUseCase.Companion.STATUS_CODE_ON_ERROR
import io.mockk.coEvery
import io.mockk.coVerify
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class CharacterListUseCaseTest : BaseCharacterUseCaseTest() {

    private lateinit var characterListUseCase: CharacterListUseCase

    @Before
    fun setup() {
        characterListUseCase = CharacterListUseCase(mockCharacterRepository)
    }

    @Test
    fun onExecution_shouldGetListOfCharacters() {
        // ARRANGE
        val offset = 0
        val query = ""
        coEvery {
            mockCharacterRepository.getCharactersList(
                any(),
                any()
            )
        } returns CHARACTER_RESULT_SUCCESS

        // ACT
        val res = runBlocking { characterListUseCase.execute(offset, query) }

        // ASSERT
        coVerify(exactly = 1) { mockCharacterRepository.getCharactersList(offset, query) }
        assertEquals(CHARACTER_RESULT_SUCCESS, res)
    }

    @Test
    fun onExecution_whenRaiseException_shouldReturnErrorResult() {
        // ARRANGE
        coEvery { mockCharacterRepository.getCharactersList(any(), any()) } throws Exception()

        // ACT
        val res = runBlocking { characterListUseCase.execute(0, "") }

        // ASSERT
        assertEquals(CHARACTER_LIST_ON_ERROR, res.data)
        assertEquals(STATUS_CODE_ON_ERROR, res.statusCode)
    }
}