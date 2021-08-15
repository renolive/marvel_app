package com.renatoaoliveira.character.presentation.ui

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.GridLayoutManager
import com.renatoaoliveira.character.R
import com.renatoaoliveira.character.databinding.BottomStatusLoadBinding
import com.renatoaoliveira.character.databinding.FragmentCharacterListBinding
import com.renatoaoliveira.character.presentation.ui.adapter.BottomAdapter
import com.renatoaoliveira.character.presentation.ui.adapter.CharacterAdapter
import com.renatoaoliveira.character.presentation.ui.decorator.CharacterItemDecorator
import com.renatoaoliveira.common.connection.checkConnectivity

abstract class BaseCharacterGridFragment : Fragment(R.layout.fragment_character_list) {

    protected abstract val viewModel: ViewModel

    protected var binding: FragmentCharacterListBinding? = null
    protected var bottomStatusBinding: BottomStatusLoadBinding? = null

    protected val characterAdapter = CharacterAdapter()
    protected val bottomAdapter = BottomAdapter()
    protected val concatAdapter = ConcatAdapter(characterAdapter, bottomAdapter)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentCharacterListBinding.bind(view).apply {
        }

        configureView()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
        bottomStatusBinding = null
    }

    private fun configureView() {
        binding?.run {
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
        }
    }

    protected fun onError() {
        if (checkConnectivity(requireContext())) showErrorScreen() else showOfflineErrorScreen()
    }

    protected fun showErrorScreen() {
        binding?.run {
            swipeRefresh.isVisible = false
            loadingScreenInclude.isVisible = false
            errorOfflineScreenInclude.isVisible = false
            errorScreenInclude.isVisible = true

        }
    }

    protected fun showOfflineErrorScreen() {
        binding?.run {
            swipeRefresh.isVisible = false
            loadingScreenInclude.isVisible = false
            errorScreenInclude.isVisible = false
            errorOfflineScreenInclude.isVisible = true
        }
    }

    protected fun showLoadingScreen() {
        binding?.run {
            swipeRefresh.isVisible = false
            errorScreenInclude.isVisible = false
            errorOfflineScreenInclude.isVisible = false
            loadingScreenInclude.isVisible = true
        }
    }

    protected fun showGridScreen() {
        binding?.run {
            swipeRefresh.isVisible = true
            errorScreenInclude.isVisible = false
            errorOfflineScreenInclude.isVisible = false
            loadingScreenInclude.isVisible = false
        }
    }
}