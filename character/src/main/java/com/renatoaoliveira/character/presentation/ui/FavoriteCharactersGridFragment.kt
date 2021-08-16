package com.renatoaoliveira.character.presentation.ui

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.renatoaoliveira.character.domain.model.Character
import com.renatoaoliveira.character.presentation.mapper.mapToModel
import com.renatoaoliveira.character.presentation.mapper.mapToVO
import com.renatoaoliveira.character.presentation.model.CharacterVO
import com.renatoaoliveira.character.presentation.ui.adapter.CharacterAdapter
import com.renatoaoliveira.character.presentation.ui.decorator.CharacterItemDecorator
import com.renatoaoliveira.character.presentation.viewmodel.CharacterFavoritesViewModel
import com.renatoaoliveira.character.presentation.viewmodel.CharacterFavoritesViewModel.CharacterFavoriteState
import org.koin.androidx.viewmodel.ext.android.getViewModel

class FavoriteCharactersGridFragment : BaseCharacterGridFragment(), CharacterViewHolderListener {

    private val favoriteAdapter = CharacterAdapter(this)

    private lateinit var favoriteViewModel: CharacterFavoritesViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        favoriteViewModel = getViewModel()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        configureView()
        configureHomeObservers()
    }

    private fun configureView() {
        binding?.run {
            characterGrid.adapter = favoriteAdapter
            characterGrid.addItemDecoration(CharacterItemDecorator())
            characterGrid.layoutManager = GridLayoutManager(requireContext(), 2)
        }
    }

    private fun configureHomeObservers() {
        with(favoriteViewModel) {
            characterFavorites.observe(viewLifecycleOwner, ::observeCharacterFavoritesState)
        }
    }

    private fun observeCharacterFavoritesState(state: CharacterFavoriteState) {
        when (state) {
            is CharacterFavoriteState.Loading -> {
                showLoadingScreen()
            }
            is CharacterFavoriteState.Success -> {
                showGridScreen()
                updateFavoriteList(state.list)
            }
            is CharacterFavoriteState.Error -> {
                onError()
            }
            else -> {
                // Do nothing
            }
        }
    }

    private fun updateFavoriteList(favoriteList: List<Character>) {
        favoriteAdapter.submitList(favoriteList.map { it.mapToVO(true) })
    }

    override fun OnClickFavorite(character: CharacterVO) {
        favoriteViewModel.onFavoriteClick(character.mapToModel(), !character.isFavorite)
    }

    override fun OnViewHolderClick(character: CharacterVO) {
        startActivity(CharacterDetailsActivity.newIntent(requireContext(), character))
    }
}