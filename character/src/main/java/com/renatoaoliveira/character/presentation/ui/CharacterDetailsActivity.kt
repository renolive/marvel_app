package com.renatoaoliveira.character.presentation.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import com.bumptech.glide.Glide
import com.renatoaoliveira.character.R
import com.renatoaoliveira.character.databinding.CharacterDetailActivityBinding
import com.renatoaoliveira.character.presentation.mapper.mapToModel
import com.renatoaoliveira.character.presentation.model.CharacterVO
import com.renatoaoliveira.character.presentation.viewmodel.CharacterFavoritesViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class CharacterDetailsActivity : AppCompatActivity() {

    private lateinit var binding: CharacterDetailActivityBinding

    private val favoriteViewModel: CharacterFavoritesViewModel by viewModel()

    private lateinit var characterInfo: CharacterVO

    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = CharacterDetailActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getIntentExtras()

        supportActionBar?.hide()
    }

    private fun getIntentExtras() {
        intent?.extras?.let { extraBundle ->
            characterInfo = extraBundle[CHARACTER_INFO] as CharacterVO
            return
        }
        Toast.makeText(this, "Erro ao abrir personagem", Toast.LENGTH_SHORT).show()
        finish()
    }

    override fun onStart() {
        super.onStart()

        configureView()
        configureObservers()
    }

    private fun configureView() {
        with(binding) {
            title?.let { characterDetailTitle.text = it }

            if (characterInfo.description.isNotEmpty()) {
                characterDetailDescription.text = characterInfo.description
            }

            Glide.with(this@CharacterDetailsActivity)
                .load(characterInfo.thumbnailUrl)
                .centerCrop()
                .placeholder(R.drawable.iron_man_placeholder)
                .dontAnimate()
                .into(characterDetailImage)

            characterDetailUp.setOnClickListener { onBackPressed() }

            favoriteIcon.setOnClickListener {
                favoriteViewModel.onFavoriteClick(
                    characterInfo.mapToModel(),
                    !characterInfo.isFavorite
                )
            }
        }
    }

    private fun configureObservers() {
        favoriteViewModel.characterFavorites.observe(
            this,
            ::observeCharacterFavoritesState
        )
    }

    private fun observeCharacterFavoritesState(state: CharacterFavoritesViewModel.CharacterFavoriteState) {
        when (state) {
            is CharacterFavoritesViewModel.CharacterFavoriteState.Success -> {
                updateFavoriteStatus(state.list.map { it.id })
            }
            else -> {
                // Do nothing
            }
        }
    }

    private fun updateFavoriteStatus(favoriteIdList: List<Int>) {
        characterInfo.isFavorite = characterInfo.id in favoriteIdList

        val backgroundLayout =
            if (characterInfo.isFavorite) R.drawable.ic_favorite_checked
            else R.drawable.ic_favorite_unchecked

        binding.favoriteIcon.background =
            ResourcesCompat.getDrawable(resources, backgroundLayout, null)
    }

    companion object {
        private const val CHARACTER_INFO = "CHARACTER_INFO"
        fun newIntent(context: Context, characterVO: CharacterVO): Intent {
            return Intent(context, CharacterDetailsActivity::class.java).apply {
                putExtra(CHARACTER_INFO, characterVO)
            }
        }
    }
}