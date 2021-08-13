package com.renatoaoliveira.character.domain.repository

data class CharacterResult<R> (
    var data: R,
    var status: Int,
    var message: String,
    var success: Boolean
)