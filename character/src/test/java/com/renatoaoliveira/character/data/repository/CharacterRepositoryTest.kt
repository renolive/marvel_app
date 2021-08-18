package com.renatoaoliveira.character.data.repository

import com.renatoaoliveira.character.data.mapper.mapToEntity
import com.renatoaoliveira.character.data.mapper.mapToModel
import com.renatoaoliveira.character.data.model.CharacterResponse
import com.renatoaoliveira.character.data.model.CharactersListResponse
import com.renatoaoliveira.character.data.model.DataResponse
import com.renatoaoliveira.character.data.model.ThumbnailResponse
import com.renatoaoliveira.character.data.repository.CharacterRepository.Companion.SEARCH_QUERY_NAME
import com.renatoaoliveira.character.data.repository.local.dao.CharacterFavoriteDao
import com.renatoaoliveira.character.data.repository.local.entity.CharacterFavoriteEntity
import com.renatoaoliveira.character.data.repository.remote.api.CharacterServiceAPI
import com.renatoaoliveira.character.domain.model.Character
import com.renatoaoliveira.character.domain.repository.CharacterResult
import io.mockk.*
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Before
import org.junit.Test
import retrofit2.Response

class CharacterRepositoryTest {

    @RelaxedMockK
    val mockCharacterServiceAPI = mockk<CharacterServiceAPI>()

    @RelaxedMockK
    val mockCharacterFavoriteDao = mockk<CharacterFavoriteDao>()

    lateinit var characterRepository: CharacterRepository

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        characterRepository = CharacterRepository(mockCharacterServiceAPI, mockCharacterFavoriteDao)
    }

    //region Database
    @Test
    fun saveCharacterAsFavorite_shouldInvokeInsertCharacterInDatabase() {
        // ARRANGE
        coEvery { mockCharacterFavoriteDao.insertCharacterFavorite(any()) } just Runs

        // ACT
        runBlocking { characterRepository.saveCharacterAsFavorite(CHARACTER) }

        // ASSERT
        coVerify(exactly = 1) { mockCharacterFavoriteDao.insertCharacterFavorite(CHARACTER.mapToEntity()) }
    }

    @Test
    fun deleteCharacterFromFavorite_shouldInvokeDeleteCharacterFromDatabase() {
        // ARRANGE
        coEvery { mockCharacterFavoriteDao.deleteCharacterFavorite(any()) } just Runs

        // ACT
        runBlocking { characterRepository.deleteCharacterFromFavorite(CHARACTER) }

        // ASSERT
        coVerify(exactly = 1) { mockCharacterFavoriteDao.deleteCharacterFavorite(CHARACTER.mapToEntity()) }
    }

    @Test
    fun getCharacterFavorites_shouldReturnFlowListOfCharacters() {
        // ARRANGE
        every { mockCharacterFavoriteDao.getCharacterFavorites() } returns
                flowOf(CHARACTER_FAVORITE_LIST)

        // ACT
        val favoriteList = runBlocking { characterRepository.getCharacterFavorites().first() }

        // ASSERT
        verify(exactly = 1) { mockCharacterFavoriteDao.getCharacterFavorites() }
        assertEquals(CHARACTER_FAVORITE_LIST.first().mapToModel(), favoriteList.first())
    }

    @Test(expected = Exception::class)
    fun saveCharacterAsFavorite_whenRaiseException_shouldThrow() {
        // ARRANGE
        coEvery { mockCharacterFavoriteDao.insertCharacterFavorite(any()) } throws Exception()

        // ACT
        runBlocking { characterRepository.saveCharacterAsFavorite(CHARACTER) }
    }

    @Test(expected = Exception::class)
    fun deleteCharacterFromFavorite_whenRaiseException_shouldThrow() {
        // ARRANGE
        coEvery { mockCharacterFavoriteDao.deleteCharacterFavorite(any()) } throws Exception()

        // ACT
        runBlocking { characterRepository.deleteCharacterFromFavorite(CHARACTER) }
    }

    @Test(expected = Exception::class)
    fun getCharacterFavorites_whenRaiseException_shouldThrow() {
        // ARRANGE
        every { mockCharacterFavoriteDao.getCharacterFavorites() } throws Exception()

        // ACT
        runBlocking { characterRepository.getCharacterFavorites() }
    }
    //endregion

    //region WebService
    @Test
    fun getCharactersList_whenHasNoQueryParameter_shouldFetchGeneralCharacterList() {
        // ARRANGE
        val slotQueryParameters = slot<Map<String, String>>()
        coEvery { mockCharacterServiceAPI.getCharactersList(capture(slotQueryParameters)) } returns
                CHARACTER_REMOTE_RESPONSE_SUCCESS

        // ACT
        val characterList = runBlocking { characterRepository.getCharactersList(0, "") }

        // ASSERT
        assertFalse(slotQueryParameters.captured.keys.contains(SEARCH_QUERY_NAME))
        assertEquals(CHARACTER_RESULT_SUCCESS, characterList)
    }

    @Test
    fun getCharactersList_whenHasQueryParameter_shouldFetchGeneralCharacterList() {
        // ARRANGE
        val slotQueryParameters = slot<Map<String, String>>()
        coEvery { mockCharacterServiceAPI.getCharactersList(capture(slotQueryParameters)) } returns
                CHARACTER_REMOTE_RESPONSE_SUCCESS

        // ACT
        val query = "query"
        val characterList = runBlocking { characterRepository.getCharactersList(0, query) }

        // ASSERT
        assertEquals(query, slotQueryParameters.captured[SEARCH_QUERY_NAME])
        assertEquals(CHARACTER_RESULT_SUCCESS, characterList)
    }

    @Test
    fun getCharactersList_whenAPIReturnError_shouldPrepareTheResultAccordingly() {
        // ARRANGE
        val slotQueryParameters = slot<Map<String, String>>()
        coEvery { mockCharacterServiceAPI.getCharactersList(capture(slotQueryParameters)) } returns
                CHARACTER_REMOTE_RESPONSE_ERROR

        // ACT
        val characterList = runBlocking { characterRepository.getCharactersList(0, "") }

        // ASSERT
        assertEquals(CHARACTER_RESULT_ERROR, characterList)
    }

    @Test(expected = Exception::class)
    fun getCharactersList_whenRaiseException_shouldThrow() {
        // ARRANGE
        val slotQueryParameters = slot<Map<String, String>>()
        coEvery { mockCharacterServiceAPI.getCharactersList(any()) } throws
                Exception()

        // ACT
        runBlocking { characterRepository.getCharactersList(0, "") }
    }
    //endregion

    companion object {
        val CHARACTER = Character(
            1,
            "name",
            "description",
            "https:tumbnail.url.ext"
        )

        val CHARACTER_FAVORITE_ENTITY = CharacterFavoriteEntity(
            1,
            "name",
            "description",
            "https:thumbnail.url.ext"
        )

        val CHARACTER_RESPONSE = CharacterResponse(
            1,
            "name",
            "description",
            ThumbnailResponse("http:thumbnail.url", "ext")
        )

        val CHARACTER_FAVORITE_LIST = listOf(CHARACTER_FAVORITE_ENTITY)

        val CHARACTER_DATA_RESPONSE = DataResponse(
            20,
            20,
            1000,
            20,
            listOf(CHARACTER_RESPONSE)
        )

        val CHARACTER_REMOTE_RESPONSE_SUCCESS =
            Response.success(CharactersListResponse(CHARACTER_DATA_RESPONSE))

        val CHARACTER_RESULT_SUCCESS = with(CHARACTER_REMOTE_RESPONSE_SUCCESS) {
            CharacterResult(
                body()?.data.mapToModel(),
                code(),
                message(),
                isSuccessful
            )
        }

        val CHARACTER_REMOTE_RESPONSE_ERROR =
            Response.error<CharactersListResponse>(
                500,
                "{ \"error\": \"Internal Server Error\" }".toResponseBody("application/json".toMediaTypeOrNull())
            )

        val CHARACTER_RESULT_ERROR = with(CHARACTER_REMOTE_RESPONSE_ERROR) {
            CharacterResult(
                body()?.data.mapToModel(),
                code(),
                message(),
                isSuccessful
            )
        }
    }
}