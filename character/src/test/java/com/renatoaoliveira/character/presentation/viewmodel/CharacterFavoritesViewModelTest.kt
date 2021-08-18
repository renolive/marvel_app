package com.renatoaoliveira.character.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.renatoaoliveira.character.domain.model.Character
import com.renatoaoliveira.character.domain.usecase.CharacterAddFavoriteUseCase
import com.renatoaoliveira.character.domain.usecase.CharacterGetFavoritesUseCase
import com.renatoaoliveira.character.domain.usecase.CharacterRemoveFavoriteUseCase
import io.mockk.*
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.setMain
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import timber.log.Timber

@ExperimentalCoroutinesApi
class CharacterFavoritesViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    val testDispatcher = TestCoroutineDispatcher()

    @RelaxedMockK
    val mockRemoveFavoriteUseCase = mockk<CharacterRemoveFavoriteUseCase>()

    @RelaxedMockK
    val mockAddFavoriteUseCase = mockk<CharacterAddFavoriteUseCase>()

    @RelaxedMockK
    val mockGetFavoritesUseCase = mockk<CharacterGetFavoritesUseCase>()

    private lateinit var characterFavoritesViewModel: CharacterFavoritesViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        Dispatchers.setMain(testDispatcher)
    }

    private fun getViewModel(): CharacterFavoritesViewModel = CharacterFavoritesViewModel(
        mockRemoveFavoriteUseCase,
        mockAddFavoriteUseCase,
        mockGetFavoritesUseCase,
        testDispatcher
    )

    @Test
    fun shouldBringFavoriteListOnStart() {
        // ARRANGE
        val favoriteList = listOf(FAVORITE)
        every { mockGetFavoritesUseCase.execute() } returns flowOf(favoriteList)

        // ACT
        characterFavoritesViewModel = getViewModel()

        // ASSERT
        val res = characterFavoritesViewModel
            .characterFavorites
            .value as? CharacterFavoritesViewModel.CharacterFavoriteState.Success
        assertNotNull(res)
        assertEquals(favoriteList, res!!.list)
    }

    @Test
    fun onGettingFavorite_whenRaiseException_shouldUpdateStateAccordingly() {
        // ARRANGE
        every { mockGetFavoritesUseCase.execute() } returns flow { throw Exception() }

        // ACT
        characterFavoritesViewModel = getViewModel()

        // ASSERT
        val res = characterFavoritesViewModel
            .characterFavorites
            .value as? CharacterFavoritesViewModel.CharacterFavoriteState.Error
        assertNotNull(res)
    }

    @Test
    fun onFavoriteClick_whenAddingFavorite_shouldUpdateTheFavoriteList() {
        // ARRANGE
        val favoriteList = mutableListOf(FAVORITE)

        every { mockGetFavoritesUseCase.execute() } returns flowOf(favoriteList)
        coEvery { mockAddFavoriteUseCase.execute(any()) } answers {
            favoriteList.add(ANOTHER_FAVORITE)
        }
        characterFavoritesViewModel = getViewModel()

        // ACT
        characterFavoritesViewModel.onFavoriteClick(ANOTHER_FAVORITE, true)

        // ASSERT
        coVerify(exactly = 1) { mockAddFavoriteUseCase.execute(ANOTHER_FAVORITE) }
        assertTrue(
            characterFavoritesViewModel
                .characterFavorites
                .value is CharacterFavoritesViewModel.CharacterFavoriteState.Success
        )
    }

    @Test
    fun onFavoriteClick_whenRemovingFavorite_shouldUpdateTheFavoriteList() {
        // ARRANGE
        val favoriteList = mutableListOf(FAVORITE, ANOTHER_FAVORITE)

        every { mockGetFavoritesUseCase.execute() } returns flowOf(favoriteList)
        coEvery { mockAddFavoriteUseCase.execute(any()) } answers {
            favoriteList.remove(ANOTHER_FAVORITE)
        }
        characterFavoritesViewModel = getViewModel()

        // ACT
        characterFavoritesViewModel.onFavoriteClick(ANOTHER_FAVORITE, false)

        // ASSERT
        coVerify { mockRemoveFavoriteUseCase.execute(ANOTHER_FAVORITE) }
        val res = characterFavoritesViewModel
            .characterFavorites
            .value as? CharacterFavoritesViewModel.CharacterFavoriteState.Success
        assertNotNull(res)
        assertEquals(favoriteList, res!!.list)
    }

    @Test
    fun onUpdatingFavorite_whenRaiseException_shouldLogError() {
        // ARRANGE
        val favoriteList = listOf(FAVORITE)
        mockkObject(Timber.Forest)
        every { mockGetFavoritesUseCase.execute() } returns flowOf(favoriteList)
        coEvery { mockAddFavoriteUseCase.execute(any()) } throws Exception()
        characterFavoritesViewModel = getViewModel()

        // ACT
        characterFavoritesViewModel.onFavoriteClick(ANOTHER_FAVORITE, true)

        // ASSERT
        coVerify(exactly = 1) { mockAddFavoriteUseCase.execute(ANOTHER_FAVORITE) }
        verify(exactly = 1) { Timber.e(any<Throwable>(), any()) }
    }

    companion object {
        val FAVORITE = Character(
            1,
            "name",
            "description",
            "https:tumbnail.url.ext"
        )

        val ANOTHER_FAVORITE = Character(
            2,
            "name",
            "description",
            "https:tumbnail.url.ext"
        )
    }
}