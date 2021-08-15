package com.renatoaoliveira.character.data.mapper

import com.renatoaoliveira.character.data.repository.local.entity.CharacterFavoriteEntity
import com.renatoaoliveira.character.domain.model.Character

fun Character.mapToEntity(): CharacterFavoriteEntity = CharacterFavoriteEntity(
    id = id,
    name = name,
    description = description,
    thumbnailUrl = thumbnailUrl
)