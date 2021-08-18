package com.renatoaoliveira.character.domain.usecase

import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class CharacterRemoveFavoriteUseCaseTest : BaseCharacterUseCaseTest() {

    private lateinit var removeFavoriteUseCase: CharacterRemoveFavoriteUseCase

    @Before
    fun setup() {
        removeFavoriteUseCase = CharacterRemoveFavoriteUseCase(mockCharacterRepository)
    }

    @Test
    fun onExecution_shouldInvokeRepositoryToRemoveCharacterFromFavorite() {
        // ARRANGE
        coEvery { mockCharacterRepository.deleteCharacterFromFavorite(any()) } just Runs

        // ACT
        runBlocking { removeFavoriteUseCase.execute(CHARACTER) }

        // ASSERT
        coVerify(exactly = 1) { mockCharacterRepository.deleteCharacterFromFavorite(CHARACTER) }
    }

    @Test(expected = Exception::class)
    fun onExecution_whenRaiseException_shouldThrow() {
        // ARRANGE
        coEvery { mockCharacterRepository.deleteCharacterFromFavorite(any()) } throws Exception()

        // ACT
        runBlocking { removeFavoriteUseCase.execute(CHARACTER) }
    }

}