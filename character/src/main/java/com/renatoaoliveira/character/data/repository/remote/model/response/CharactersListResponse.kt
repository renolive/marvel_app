package com.renatoaoliveira.character.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class CharactersListResponse(
    val data: DataResponse
) : Parcelable

@Parcelize
data class DataResponse(
    val offset: Int,
    val limit: Int,
    val total: Int,
    val count: Int,
    val results: List<CharacterResponse>
) : Parcelable