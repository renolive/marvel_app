package com.renatoaoliveira.character.presentation.ui

import com.renatoaoliveira.character.presentation.model.CharacterVO

interface CharacterViewHolderListener : OnClickFavoriteListener {

    override fun OnClickFavorite(character: CharacterVO)

    fun OnViewHolderClick(character: CharacterVO)
}