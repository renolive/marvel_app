package com.renatoaoliveira.character.domain.repository

data class CharacterResult<R> (
    var data: R,
    var statusCode: Int,
    var message: String,
    var success: Boolean
)