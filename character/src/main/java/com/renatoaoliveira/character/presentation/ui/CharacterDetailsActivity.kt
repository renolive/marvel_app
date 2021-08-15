package com.renatoaoliveira.character.presentation.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.renatoaoliveira.character.R
import com.renatoaoliveira.character.databinding.CharacterDetailActivityBinding
import com.renatoaoliveira.character.presentation.model.CharacterVO

class CharacterDetailsActivity : AppCompatActivity() {

    private lateinit var binding: CharacterDetailActivityBinding

    private var imageUrl: String = ""
    private var title: String? = null
    private var description: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = CharacterDetailActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getIntentExtras()
    }

    private fun getIntentExtras() {
        intent?.extras?.let { extraBundle ->
            val characterInfo = extraBundle[CHARACTER_INFO] as CharacterVO
            imageUrl = characterInfo.thumbnailUrl
            title = characterInfo.name
            description = characterInfo.description
        }
    }

    override fun onStart() {
        super.onStart()

        configureView()
    }

    private fun configureView() {
        with(binding) {
            title?.let { characterDetailTitle.text = it }

            if (!description.isNullOrEmpty()) {
                characterDetailDescription.text = description
            }

            Glide.with(this@CharacterDetailsActivity)
                .load(imageUrl)
                .centerCrop()
                .placeholder(R.drawable.iron_man_placeholder)
                .dontAnimate()
                .into(characterDetailImage)
        }
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