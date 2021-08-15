package com.renatoaoliveira.character.presentation.mapper

import com.renatoaoliveira.character.domain.model.Character
import com.renatoaoliveira.character.presentation.model.CharacterVO

fun CharacterVO.mapToModel(): Character = Character(
    id = id,
    name = name,
    description = description,
    thumbnailUrl = thumbnailUrl
)