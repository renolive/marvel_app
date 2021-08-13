package com.renatoaoliveira.marvel.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.renatoaoliveira.character.presentation.viewmodel.CharactersListViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeActivity : AppCompatActivity() {

    val viewModel: CharactersListViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        viewModel.fetchList()
        viewModel.searchCharacter("spider")

        viewModel.characterList.observe(this) {
            if (it is CharactersListViewModel.CharacterListState.Success)
                viewModel.favoriteCharacter(it.characters.first())
        }
    }
}