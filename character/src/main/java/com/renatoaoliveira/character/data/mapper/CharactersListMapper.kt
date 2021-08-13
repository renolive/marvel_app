package com.renatoaoliveira.character.data.mapper

import com.renatoaoliveira.character.data.model.DataResponse
import com.renatoaoliveira.character.domain.model.CharacterList

fun DataResponse?.mapToModel(): CharacterList {
    this ?: return CharacterList(0, 0, 0, emptyList())

    return CharacterList(
        offset = offset,
        count = count,
        total = total,
        list = this.results.map { it.mapToModel() }
    )
}