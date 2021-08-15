package com.renatoaoliveira.character.data.repository

import com.example.android.core.BuildConfig
import com.renatoaoliveira.character.data.mapper.mapToEntity
import com.renatoaoliveira.character.data.mapper.mapToModel
import com.renatoaoliveira.character.data.repository.local.dao.CharacterFavoriteDao
import com.renatoaoliveira.character.data.repository.local.entity.CharacterFavoriteEntity
import com.renatoaoliveira.character.data.repository.remote.api.CharacterServiceAPI
import com.renatoaoliveira.character.domain.model.Character
import com.renatoaoliveira.character.domain.model.CharacterList
import com.renatoaoliveira.character.domain.repository.CharacterResult
import com.renatoaoliveira.character.domain.repository.ICharacterRepository
import com.renatoaoliveira.common.extension.digestMD5
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.transform
import kotlinx.coroutines.withContext

class CharacterRepository(
    private val characterServiceAPI: CharacterServiceAPI,
    private val characterFavoriteDao: CharacterFavoriteDao
) : ICharacterRepository {
    private val apikey = BuildConfig.PUBLIC_KEY
    private val privateApikey = BuildConfig.PRIVATE_KEY
    private var timeStamp: Long = 0L
    private var hash: String = ""

    //region Database
    override suspend fun saveCharacterAsFavorite(character: Character) =
        withContext(Dispatchers.IO) {
            characterFavoriteDao.insertCharacterFavorite(character.mapToEntity())
        }

    override suspend fun deleteCharacterFromFavorite(character: Character) =
        withContext(Dispatchers.IO) {
            characterFavoriteDao.deleteCharacterFavorite(character.mapToEntity())
        }

    override fun getCharacterFavorites(): Flow<List<Character>> =
        characterFavoriteDao.getCharacterFavorites()
            .flowOn(Dispatchers.IO)
            .transform { list ->
                list.map { entity -> entity.mapToModel() }
            }

    //endregion

    //region WebService
    override suspend fun getCharactersList(offset: Int): CharacterResult<CharacterList> =
        withContext(Dispatchers.IO) {
            characterServiceAPI.getCharactersList(getQueryParams(offset)).run {
                CharacterResult(
                    body()?.data.mapToModel(),
                    code(),
                    message(),
                    isSuccessful
                )
            }
        }

    override suspend fun searchCharacters(
        offset: Int,
        query: String
    ): CharacterResult<CharacterList> =
        withContext(Dispatchers.IO) {
            characterServiceAPI.searchCharacters(getSearchQueryParams(offset, query)).run {
                CharacterResult(
                    body()?.data.mapToModel(),
                    code(),
                    message(),
                    isSuccessful
                )
            }
        }
    //endregion

    private fun updateHash() {
        hash = HASH_TEMPLATE.format(timeStamp, privateApikey, apikey).digestMD5()
    }

    @Synchronized
    private fun getQueryParams(offset: Int): Map<String, String> {
        timeStamp += 1L
        updateHash()

        return mapOf(
            TIME_STAMP_QUERY_NAME to timeStamp.toString(),
            API_KEY_QUERY_NAME to apikey,
            HASH_QUERY_NAME to hash,
            OFFSET_QUERY_NAME to offset.toString()
        )
    }

    private fun getSearchQueryParams(offset: Int, query: String): Map<String, String> {
        return getQueryParams(offset).toMutableMap().apply {
            put(SEARCH_QUERY_NAME, query)
        }
    }

    companion object {
        const val HASH_TEMPLATE = "%d%s%s"
        const val TIME_STAMP_QUERY_NAME = "ts"
        const val API_KEY_QUERY_NAME = "apikey"
        const val HASH_QUERY_NAME = "hash"
        const val OFFSET_QUERY_NAME = "offset"
        const val SEARCH_QUERY_NAME = "nameStartsWith"
    }
}