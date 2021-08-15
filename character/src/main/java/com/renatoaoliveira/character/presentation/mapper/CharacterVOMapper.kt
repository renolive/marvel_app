package com.renatoaoliveira.character.presentation.mapper

import com.renatoaoliveira.character.presentation.model.CharacterVO
import com.renatoaoliveira.character.domain.model.Character

fun Character.mapToVO(isFavorite: Boolean): CharacterVO = CharacterVO(
    id = id,
    name = name,
    description = description,
    thumbnailUrl = thumbnailUrl,
    isFavorite = isFavorite
)