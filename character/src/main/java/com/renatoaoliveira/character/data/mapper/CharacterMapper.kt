package com.renatoaoliveira.character.data.mapper

import com.renatoaoliveira.character.data.model.CharacterResponse
import com.renatoaoliveira.character.data.repository.local.entity.CharacterFavoriteEntity
import com.renatoaoliveira.character.domain.model.Character

fun CharacterResponse.mapToModel(): Character = Character(
    id = id,
    name = name,
    description = description,
    thumbnailUrl = thumbnail.path.replace("http", "https") + "." + thumbnail.extension
)

fun CharacterFavoriteEntity.mapToModel(): Character = Character(
    id = id,
    name = name,
    description = description,
    thumbnailUrl = thumbnailUrl
)