package com.renatoaoliveira.character.domain.usecase

import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class CharacterAddFavoriteUseCaseTest : BaseCharacterUseCaseTest() {

    private lateinit var addFavoriteUseCase: CharacterAddFavoriteUseCase

    @Before
    fun setup() {
        addFavoriteUseCase = CharacterAddFavoriteUseCase(mockCharacterRepository)
    }

    @Test
    fun onExecution_shouldInvokeRepositoryToSaveCharacterAsFavorite() {
        // ARRANGE
        coEvery { mockCharacterRepository.saveCharacterAsFavorite(any()) } just Runs

        // ACT
        runBlocking { addFavoriteUseCase.execute(CHARACTER) }

        // ASSERT
        coVerify(exactly = 1) { mockCharacterRepository.saveCharacterAsFavorite(CHARACTER) }
    }

    @Test(expected = Exception::class)
    fun onExecution_whenRaiseException_shouldThrow() {
        // ARRANGE
        coEvery { mockCharacterRepository.saveCharacterAsFavorite(any()) } throws Exception()

        // ACT
        runBlocking { addFavoriteUseCase.execute(CHARACTER) }
    }
}