package com.renatoaoliveira.character.data.mapper

import com.renatoaoliveira.character.data.repository.local.entity.CharacterFavorite
import com.renatoaoliveira.character.domain.model.Character

fun Character.mapToEntity(): CharacterFavorite = CharacterFavorite(
    id = id,
    name = name,
    description = description,
    thumbnailPath = thumbnailPath,
    thumbnailExtension = thumbnailExtension
)