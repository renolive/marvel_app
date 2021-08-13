package com.renatoaoliveira.character.data.mapper

import com.renatoaoliveira.character.data.model.CharacterResponse
import com.renatoaoliveira.character.domain.model.Character

fun CharacterResponse.mapToModel(): Character = Character(
    id = id,
    name = name,
    description = description,
    thumbnailPath = thumbnail.path,
    thumbnailExtension = thumbnail.extension
)