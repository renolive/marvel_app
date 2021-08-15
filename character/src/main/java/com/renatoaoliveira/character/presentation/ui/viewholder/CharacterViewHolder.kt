package com.renatoaoliveira.character.presentation.ui.viewholder

import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.renatoaoliveira.character.R
import com.renatoaoliveira.character.databinding.ViewHolderCharacterBinding
import com.renatoaoliveira.character.presentation.model.CharacterVO
import com.renatoaoliveira.character.presentation.ui.OnClickFavoriteListener

class CharacterViewHolder(private val binding: ViewHolderCharacterBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: CharacterVO, listener: OnClickFavoriteListener) {
        with(binding) {
            favoriteIcon.setOnClickListener { listener.OnClick(item) }
            if (item.isFavorite) {
                favoriteIcon.background =
                    ResourcesCompat.getDrawable(
                        itemView.resources,
                        R.drawable.ic_favorite_checked,
                        null
                    )
            }

            this.characterName.text = item.name

            Glide.with(itemView.context)
//                .load(item.thumbnailUrl)
                    //TODO remover
                .load("")
                .centerCrop()
                .placeholder(R.drawable.iron_man_placeholder)
                .dontAnimate()
                .into(characterImage)
        }
    }

}