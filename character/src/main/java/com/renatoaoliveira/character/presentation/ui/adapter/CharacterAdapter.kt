package com.renatoaoliveira.character.presentation.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.renatoaoliveira.character.databinding.ViewHolderCharacterBinding
import com.renatoaoliveira.character.presentation.ui.viewholder.CharacterViewHolder
import com.renatoaoliveira.character.presentation.model.CharacterVO
import com.renatoaoliveira.character.presentation.ui.OnClickFavoriteListener

class CharacterAdapter(private val favoriteListener: OnClickFavoriteListener) :
    ListAdapter<CharacterVO, CharacterViewHolder>(diffUtilCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterViewHolder {
        val binding = ViewHolderCharacterBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CharacterViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CharacterViewHolder, position: Int) =
        holder.bind(getItem(position), favoriteListener)

    companion object {
        private val diffUtilCallback = object : DiffUtil.ItemCallback<CharacterVO>() {
            override fun areItemsTheSame(
                oldItem: CharacterVO,
                newItem: CharacterVO
            ): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(
                oldItem: CharacterVO,
                newItem: CharacterVO
            ): Boolean =
                oldItem == newItem
        }
    }
}