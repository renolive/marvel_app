package com.renatoaoliveira.character.presentation.ui

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.GridLayoutManager
import com.renatoaoliveira.character.presentation.mapper.mapToModel
import com.renatoaoliveira.character.presentation.model.CharacterVO
import com.renatoaoliveira.character.presentation.ui.adapter.BottomAdapter
import com.renatoaoliveira.character.presentation.ui.adapter.CharacterAdapter
import com.renatoaoliveira.character.presentation.ui.decorator.CharacterItemDecorator
import com.renatoaoliveira.character.presentation.viewmodel.CharactersListViewModel
import com.renatoaoliveira.character.presentation.viewmodel.CharactersListViewModel.CharacterListState
import com.renatoaoliveira.common.connection.checkConnectivity
import com.renatoaoliveira.common.utils.RecyclerViewScrollLoader
import org.koin.androidx.viewmodel.ext.android.getViewModel

class HomeCharactersGridFragment : BaseCharacterGridFragment(), CharacterViewHolderListener {

    private lateinit var charactersViewModel: CharactersListViewModel

    private val characterAdapter = CharacterAdapter(this)
    private val bottomAdapter = BottomAdapter()
    private val concatAdapter = ConcatAdapter(characterAdapter, bottomAdapter)

    private val recyclerScrollLoader =
        RecyclerViewScrollLoader(POSITION_THRESHOLD, ::fetchMoreCharacters)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        charactersViewModel = getViewModel()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fetchFirstCharacters()
        configureView()
        configureObservers()
    }

    private fun fetchFirstCharacters() = charactersViewModel.fetchList()

    private fun fetchMoreCharacters() = charactersViewModel.fetchList(false)

    private fun configureView() {
        binding?.run {
            characterGrid.itemAnimator = null
            characterGrid.adapter = concatAdapter
            characterGrid.addItemDecoration(CharacterItemDecorator())
            characterGrid.layoutManager = GridLayoutManager(requireContext(), 2).apply {
                spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                    override fun getSpanSize(position: Int): Int {
                        // If last position is the bottom item
                        return if (position == itemCount - 1 && bottomAdapter.itemCount == 1) 2 else 1
                    }
                }
            }
            characterGrid.addOnScrollListener(recyclerScrollLoader)
        }
    }

    private fun configureObservers() {
        with(charactersViewModel) {
            characterList.observe(viewLifecycleOwner, ::observeCharactersListState)
            charactersLiveDataMerger.observe(viewLifecycleOwner, ::observeCharacterMergedData)
        }
    }

    private fun observeCharactersListState(state: CharacterListState) {
        when (state) {
            is CharacterListState.Loading -> {
                if (characterAdapter.currentList.isEmpty()) {
                    showLoadingScreen()
                } else {
                    bottomAdapter.setStatus(BottomAdapter.BOTTOM_STATUS.LOADING)
                }
                return
            }
            is CharacterListState.Error -> {
                onError()
            }
            else -> {
                // Do nothing
            }
        }
        recyclerScrollLoader.setNotLoading()
    }

    private fun observeCharacterMergedData(list: List<CharacterVO>) {
        updateCharacterList(list)
        showGridScreen()
    }

    private fun updateCharacterList(newCharacterList: List<CharacterVO>) {
        characterAdapter.submitList(newCharacterList)
        bottomAdapter.setStatus(BottomAdapter.BOTTOM_STATUS.EMPTY)
    }

    override fun OnClickFavorite(characterVO: CharacterVO) {
        charactersViewModel.onFavoriteClick(characterVO.mapToModel(), !characterVO.isFavorite)
    }

    override fun OnViewHolderClick(character: CharacterVO) {
        startActivity(CharacterDetailsActivity.newIntent(requireContext(), character))
    }

    override fun onError() {
        when {
            characterAdapter.currentList.isEmpty() -> super.onError()
            checkConnectivity(requireContext()) ->
                bottomAdapter.setStatus(BottomAdapter.BOTTOM_STATUS.OFFLINE)
            else -> bottomAdapter.setStatus(BottomAdapter.BOTTOM_STATUS.ERROR)
        }
    }

    companion object {
        const val POSITION_THRESHOLD = 1
    }
}