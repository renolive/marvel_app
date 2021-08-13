package com.renatoaoliveira.character.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CharacterResponse(
    val id: Int,
    val name: String,
    val description: String,
    val thumbnail: ThumbnailResponse
) : Parcelable

@Parcelize
data class ThumbnailResponse(
    val path: String,
    val extension: String
) : Parcelable