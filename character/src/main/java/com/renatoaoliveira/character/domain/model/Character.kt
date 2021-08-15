package com.renatoaoliveira.character.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CharacterList(
    val offset: Int,
    val count: Int,
    val total: Int,
    val list: List<Character>
) : Parcelable

@Parcelize
data class Character(
    val id: Int,
    val name: String,
    val description: String,
    val thumbnailUrl: String
) : Parcelable