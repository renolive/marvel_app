package com.renatoaoliveira.character.data.model.mapper

import com.renatoaoliveira.character.data.model.CharacterResponse
import com.renatoaoliveira.character.data.model.DataResponse
import com.renatoaoliveira.character.domain.model.Character
import com.renatoaoliveira.character.domain.model.CharacterList
import okhttp3.Challenge

fun DataResponse?.mapToModel(): CharacterList {
    this ?: return CharacterList(0, 0, 0, emptyList())

    return CharacterList(
        offset = offset,
        count = count,
        total = total,
        list = this.results.map { it.mapToModel() }
    )
}

fun CharacterResponse.mapToModel(): Character = Character(
    id = id,
    name = name,
    description = description,
    thumbnailPath = thumbnail.path,
    thumbnailExtension = thumbnail.extension
)