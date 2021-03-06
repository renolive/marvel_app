package com.renatoaoliveira.character.data.repository.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CharacterFavoriteEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val description: String,
    val thumbnailUrl: String
)