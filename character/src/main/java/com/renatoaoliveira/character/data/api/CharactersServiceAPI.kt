package com.renatoaoliveira.character.data.api

import com.renatoaoliveira.character.data.model.CharactersListResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface CharactersServiceAPI {
    /**
     * API service to fetch list of characters
     */
    @GET("/v1/public/characters")
    suspend fun getCharactersList(
        @QueryMap params: Map<String, String>
    ): Response<CharactersListResponse>
}

