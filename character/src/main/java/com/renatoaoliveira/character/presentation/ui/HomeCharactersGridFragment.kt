package com.renatoaoliveira.character.presentation.ui

import android.os.Bundle
import android.view.View
import com.renatoaoliveira.character.domain.model.Character
import com.renatoaoliveira.character.presentation.ui.adapter.BottomAdapter
import com.renatoaoliveira.character.presentation.viewmodel.CharactersListViewModel
import com.renatoaoliveira.character.presentation.viewmodel.CharactersListViewModel.CharacterListState
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeCharactersGridFragment : BaseCharacterGridFragment() {

    override val viewModel: CharactersListViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fetchFirstCharacters()
        configureHomeObservers()
    }

    private fun fetchFirstCharacters() = viewModel.fetchList()

    private fun configureHomeObservers() {
        with(viewModel) {
            characterList.observe(viewLifecycleOwner, ::observeCharactersListState)
            characterSearchList.observe(viewLifecycleOwner, ::observeCharactersListState)
        }
    }

    private fun observeCharactersListState(state: CharacterListState) {
        when (state) {
            is CharacterListState.Loading -> {
                showLoadingScreen()
            }
            is CharacterListState.Success -> {
                showGridScreen()
                updateCharacterList(state.characters)
                viewModel.resetCharacterList()
            }
            is CharacterListState.Error -> {
                onError()
            }
            else -> {
                // Do nothing
            }
        }
    }

    private fun updateCharacterList(characterList: List<Character>) {
        characterAdapter.submitList(characterList.mapToVO())
        bottomAdapter.setStatus(BottomAdapter.BOTTOM_STATUS.LOADING)
    }

    private fun observeCharactersListState(state: CharactersListViewModel.CharacterSearchListState) {

    }
}