package com.renatoaoliveira.character.data.repository

import com.example.android.core.BuildConfig
import com.renatoaoliveira.character.data.api.CharactersServiceAPI
import com.renatoaoliveira.character.data.model.mapper.mapToModel
import com.renatoaoliveira.character.domain.model.CharacterList
import com.renatoaoliveira.character.domain.repository.CharacterResult
import com.renatoaoliveira.character.domain.repository.ICharacterRepository
import com.renatoaoliveira.character.utils.digestMD5
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CharacterRepository(
    val charactersServiceAPI: CharactersServiceAPI
) : ICharacterRepository {
    private val apikey = BuildConfig.PUBLIC_KEY
    private val privateApikey = BuildConfig.PRIVATE_KEY
    private var timeStamp: Long = 0L
    private var hash: String = ""

    override suspend fun getCharactersList(offset: Int): CharacterResult<CharacterList> =
        withContext(Dispatchers.IO) {
            try {
                charactersServiceAPI.getCharactersList(getQueryParams(offset)).run {
                    CharacterResult(
                        body()?.data.mapToModel(),
                        code(),
                        message(),
                        isSuccessful
                    )
                }
            } catch (e: Exception) {
                CharacterResult(
                    CharacterList(0, 0, 0, emptyList()),
                    0,
                    e.message.orEmpty(),
                    false
                )
            }
        }

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

    private fun updateHash() {
        hash = HASH_TEMPLATE.format(timeStamp, privateApikey, apikey).digestMD5()
    }

    companion object {
        const val HASH_TEMPLATE = "%d%s%s"
        const val TIME_STAMP_QUERY_NAME = "ts"
        const val API_KEY_QUERY_NAME = "apikey"
        const val HASH_QUERY_NAME = "hash"
        const val OFFSET_QUERY_NAME = "offset"
    }
}