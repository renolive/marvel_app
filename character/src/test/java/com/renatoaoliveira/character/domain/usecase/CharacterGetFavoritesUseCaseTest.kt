package com.renatoaoliveira.character.domain.usecase

import io.mockk.every
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class CharacterGetFavoritesUseCaseTest : BaseCharacterUseCaseTest() {

    private lateinit var getFavoritesUseCase: CharacterGetFavoritesUseCase

    @Before
    fun setup() {
        getFavoritesUseCase = CharacterGetFavoritesUseCase(mockCharacterRepository)
    }

    @Test
    fun onExecution_shouldGetListOfFavoriteCharacter() {
        // ARRANGE
        val favoriteList = listOf(CHARACTER)
        every { mockCharacterRepository.getCharacterFavorites() } returns flowOf(favoriteList)

        // ACT
        val res = runBlocking { getFavoritesUseCase.execute().first() }

        // ASSERT
        assertEquals(favoriteList, res)
    }

    @Test(expected = Exception::class)
    fun onExecution_whenRaiseException_shouldThrow() {
        // ARRANGE
        every { mockCharacterRepository.getCharacterFavorites() } throws Exception()

        // ACT
        runBlocking { getFavoritesUseCase.execute().first() }
    }
}