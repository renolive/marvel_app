package com.renatoaoliveira.character.presentation.ui.viewholder

import com.renatoaoliveira.character.presentation.model.CharacterVO
import com.renatoaoliveira.character.presentation.ui.OnClickFavoriteListener

interface CharacterViewHolderListener : OnClickFavoriteListener {

    override fun OnClickFavorite(character: CharacterVO)

    fun OnViewHolderClick(character: CharacterVO)
}