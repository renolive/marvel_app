package com.renatoaoliveira.character.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.renatoaoliveira.character.domain.model.Character
import com.renatoaoliveira.character.domain.model.CharacterList
import com.renatoaoliveira.character.domain.repository.CharacterResult
import com.renatoaoliveira.character.domain.usecase.CharacterAddFavoriteUseCase
import com.renatoaoliveira.character.domain.usecase.CharacterGetFavoritesUseCase
import com.renatoaoliveira.character.domain.usecase.CharacterListUseCase
import com.renatoaoliveira.character.domain.usecase.CharacterRemoveFavoriteUseCase
import com.renatoaoliveira.character.presentation.mapper.mapToVO
import io.mockk.*
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.setMain
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class CharactersListViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()


    @RelaxedMockK
    val mockListUseCase = mockk<CharacterListUseCase>()

    @RelaxedMockK
    val mockAddFavoriteUseCase = mockk<CharacterAddFavoriteUseCase>()

    @RelaxedMockK
    val mockRemoveFavoriteUseCase = mockk<CharacterRemoveFavoriteUseCase>()

    @RelaxedMockK
    val mockGetFavoritesUseCase = mockk<CharacterGetFavoritesUseCase>()

    private val testDispatcher = TestCoroutineDispatcher()

    private lateinit var charactersListViewModel: CharactersListViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        Dispatchers.setMain(testDispatcher)
        initialMock()
    }

    private fun getViewModel(): CharactersListViewModel = CharactersListViewModel(
        mockListUseCase,
        mockAddFavoriteUseCase,
        mockRemoveFavoriteUseCase,
        mockGetFavoritesUseCase,
        testDispatcher
    )

    private fun initialMock() {
        val favoriteList = emptyList<Character>()
        every { mockGetFavoritesUseCase.execute() } returns flowOf(favoriteList)
    }

    @Test
    fun fetchFirstCharacters_shouldGetCharactersListWithOffsetZero() {
        // ARRANGE
        val characterResult = mockCharacterResultSuccess(mockCharacterListResult(CHARACTER))
        coEvery { mockListUseCase.execute(any(), any()) } returns characterResult
        charactersListViewModel = getViewModel()

        // ACT
        runBlocking { charactersListViewModel.fetchFirstCharacters("") }

        // ASSERT
        coVerify(exactly = 1) { mockListUseCase.execute(0, "") }
        val res = charactersListViewModel
            .characterList
            .value as? CharactersListViewModel.CharacterListState.Success
        Assert.assertNotNull(res)
        Assert.assertEquals(listOf(CHARACTER), res!!.characters)
    }

    @Test
    fun fetchFirstCharacters_whenHasQuery_shouldGetCharactersListByQueryAndWithOffsetZero() {
        // ARRANGE
        val query = "query"
        val characterResult = mockCharacterResultSuccess(mockCharacterListResult(CHARACTER))
        coEvery { mockListUseCase.execute(any(), any()) } returns characterResult
        charactersListViewModel = getViewModel()

        // ACT
        runBlocking { charactersListViewModel.fetchFirstCharacters(query) }

        // ASSERT
        coVerify(exactly = 1) { mockListUseCase.execute(0, query) }
        val res = charactersListViewModel
            .characterList
            .value as? CharactersListViewModel.CharacterListState.Success
        Assert.assertNotNull(res)
        Assert.assertEquals(listOf(CHARACTER), res!!.characters)
    }

    @Test
    fun fetchFirstCharacters_whenResultHasError_shouldUpdateStateAccordingly() {
        // ARRANGE
        val characterResult = mockCharacterResultError()
        coEvery { mockListUseCase.execute(any(), any()) } returns characterResult
        charactersListViewModel = getViewModel()

        // ACT
        runBlocking { charactersListViewModel.fetchFirstCharacters("") }

        // ASSERT
        coVerify(exactly = 1) { mockListUseCase.execute(0, "") }
        assertTrue(
            charactersListViewModel
                .characterList
                .value is CharactersListViewModel.CharacterListState.Error
        )
    }

    @Test
    fun fetchFirstCharacters_whenRaiseException_shouldUpdateStateAccordingly() {
        // ARRANGE
        coEvery { mockListUseCase.execute(any(), any()) } throws Exception()
        charactersListViewModel = getViewModel()

        // ACT
        runBlocking { charactersListViewModel.fetchFirstCharacters("") }

        // ASSERT
        coVerify(exactly = 1) { mockListUseCase.execute(0, "") }
        assertTrue(
            charactersListViewModel
                .characterList
                .value is CharactersListViewModel.CharacterListState.Error
        )
    }

    @Test
    fun fetchNextCharacters_shouldGetCharactersListWithOffsetUpdated() {
        // ARRANGE
        val characterResult = mockCharacterResultSuccess(mockCharacterListResult(CHARACTER))
        coEvery { mockListUseCase.execute(any(), any()) } returns characterResult
        charactersListViewModel = getViewModel()

        // ACT
        runBlocking {
            charactersListViewModel.fetchFirstCharacters("")
            // offset is updated after first search
            charactersListViewModel.fetchNextCharacters()
        }

        // ASSERT
        coVerify(exactly = 1) { mockListUseCase.execute(characterResult.data.count, "") }
        val res = charactersListViewModel
            .characterList
            .value as? CharactersListViewModel.CharacterListState.Success
        Assert.assertNotNull(res)
        Assert.assertEquals(listOf(CHARACTER), res!!.characters)
    }

    @Test
    fun fetchNextCharacters_whenHasQuerySet_shouldGetCharactersByQueryListWithOffsetUpdated() {
        // ARRANGE
        val query = "query"
        val characterResult = mockCharacterResultSuccess(mockCharacterListResult(CHARACTER))
        coEvery { mockListUseCase.execute(any(), any()) } returns characterResult
        charactersListViewModel = getViewModel()

        // ACT
        runBlocking {
            charactersListViewModel.fetchFirstCharacters(query)
            // offset is updated after first search
            charactersListViewModel.fetchNextCharacters()
        }

        // ASSERT
        coVerify(exactly = 1) { mockListUseCase.execute(characterResult.data.count, query) }
        val res = charactersListViewModel
            .characterList
            .value as? CharactersListViewModel.CharacterListState.Success
        Assert.assertNotNull(res)
        Assert.assertEquals(listOf(CHARACTER), res!!.characters)
    }

    @Test
    fun onFetchCharacters_whenSuccess_shouldUpdateCharactersList() {
        // ARRANGE
        val characterResult =
            mockCharacterResultSuccess(mockCharacterListResult(CHARACTER, CHARACTER))
        coEvery { mockListUseCase.execute(any(), any()) } returns characterResult
        charactersListViewModel = getViewModel()
        charactersListViewModel.charactersLiveDataMerger.observeForever() {}

        // ACT
        runBlocking { charactersListViewModel.fetchFirstCharacters("") }

        // ASSERT
        val res = charactersListViewModel.charactersLiveDataMerger.value
        val characterVO = CHARACTER.mapToVO(false)
        assertEquals(listOf(characterVO, characterVO), res)
    }

    @Test
    fun onFetchCharacters_whenCharacterIsFavorite_shouldUpdateCharactersListCorrectly() {
        // ARRANGE
        val favoriteList = listOf(CHARACTER)
        val characterResult = mockCharacterResultSuccess(mockCharacterListResult(CHARACTER))
        every { mockGetFavoritesUseCase.execute() } returns flowOf(favoriteList)
        coEvery { mockListUseCase.execute(any(), any()) } returns characterResult
        charactersListViewModel = getViewModel()
        charactersListViewModel.charactersLiveDataMerger.observeForever() {}

        // ACT
        runBlocking { charactersListViewModel.fetchFirstCharacters("") }

        // ASSERT
        val res = charactersListViewModel.charactersLiveDataMerger.value
        val characterVO = CHARACTER.mapToVO(true)
        assertEquals(listOf(characterVO), res)
    }

    @Test
    fun onFavoriteClick_whenMarkAsFavoriteAndCharacterIsInList_shouldUpdateCharactersListCorrectly() {
        // ARRANGE
        val mutableFavoriteList = MutableStateFlow(emptyList<Character>())
        val characterResult = mockCharacterResultSuccess(mockCharacterListResult(CHARACTER))
        every { mockGetFavoritesUseCase.execute() } returns mutableFavoriteList
        coEvery { mockListUseCase.execute(any(), any()) } returns characterResult
        coEvery { mockAddFavoriteUseCase.execute(any()) } answers {
            mutableFavoriteList.value = listOf(CHARACTER)
        }
        charactersListViewModel = getViewModel()
        charactersListViewModel.charactersLiveDataMerger.observeForever() {}

        // ACT
        runBlocking {
            charactersListViewModel.fetchFirstCharacters("")
            charactersListViewModel.onFavoriteClick(CHARACTER, true)
        }

        // ASSERT
        coVerify(exactly = 1) { mockAddFavoriteUseCase.execute(CHARACTER) }
        val res = charactersListViewModel.charactersLiveDataMerger.value
        val characterVO = CHARACTER.mapToVO(true)
        assertEquals(listOf(characterVO), res)
    }

    @Test
    fun onFavoriteClick_whenUnmarkFavoriteAndCharacterIsInList_shouldUpdateCharactersListCorrectly() {
        // ARRANGE
        val mutableFavoriteList = MutableStateFlow(listOf(CHARACTER))
        val characterResult = mockCharacterResultSuccess(mockCharacterListResult(CHARACTER))
        every { mockGetFavoritesUseCase.execute() } returns mutableFavoriteList
        coEvery { mockListUseCase.execute(any(), any()) } returns characterResult
        coEvery { mockRemoveFavoriteUseCase.execute(any()) } answers {
            mutableFavoriteList.value = emptyList()
        }
        charactersListViewModel = getViewModel()
        charactersListViewModel.charactersLiveDataMerger.observeForever() {}

        // ACT
        runBlocking {
            charactersListViewModel.fetchFirstCharacters("")
            charactersListViewModel.onFavoriteClick(CHARACTER, false)
        }

        // ASSERT
        coVerify(exactly = 1) { mockRemoveFavoriteUseCase.execute(CHARACTER) }
        val res = charactersListViewModel.charactersLiveDataMerger.value
        val characterVO = CHARACTER.mapToVO(false)
        assertEquals(listOf(characterVO), res)
    }

    companion object {
        val CHARACTER = Character(
            1,
            "name",
            "description",
            "https:tumbnail.url.ext"
        )

        fun mockCharacterListResult(vararg characters: Character) = CharacterList(
            0,
            20,
            1000,
            listOf(*characters)
        )

        fun mockCharacterResultSuccess(characterList: CharacterList) = CharacterResult(
            characterList,
            200,
            "message",
            true
        )

        fun mockCharacterResultError(code: Int = 500) = CharacterResult(
            mockCharacterListResult(),
            code,
            "message",
            false
        )
    }
}