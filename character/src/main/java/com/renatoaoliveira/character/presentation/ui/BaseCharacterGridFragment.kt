package com.renatoaoliveira.character.presentation.ui

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.renatoaoliveira.character.R
import com.renatoaoliveira.character.databinding.BottomStatusLoadBinding
import com.renatoaoliveira.character.databinding.FragmentCharacterListBinding
import com.renatoaoliveira.common.connection.hasConnection

abstract class BaseCharacterGridFragment : Fragment(R.layout.fragment_character_list) {

    protected var binding: FragmentCharacterListBinding? = null
    protected var bottomStatusBinding: BottomStatusLoadBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentCharacterListBinding.bind(view)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
        bottomStatusBinding = null
    }

    protected open fun onError() {
        if (hasConnection(requireContext())) showErrorScreen() else showOfflineErrorScreen()
    }

    private fun hideAllScreens() {
        binding?.run {
            swipeRefresh.isVisible = false
            loadingScreenInclude.isVisible = false
            errorOfflineScreenInclude.isVisible = false
            errorScreenInclude.isVisible = false
            emptyScreenInclude.isVisible = true
        }
    }

    protected fun showEmptyScreen() {
        binding?.run {
            hideAllScreens()
            emptyScreenInclude.isVisible = true
        }
    }

    protected fun showErrorScreen() {
        binding?.run {
            hideAllScreens()
            errorScreenInclude.isVisible = true
        }
    }

    protected fun showOfflineErrorScreen() {
        binding?.run {
            hideAllScreens()
            errorOfflineScreenInclude.isVisible = true
        }
    }

    protected fun showLoadingScreen() {
        binding?.run {
            hideAllScreens()
            loadingScreenInclude.isVisible = true
        }
    }

    protected fun showGridScreen() {
        binding?.run {
            hideAllScreens()
            swipeRefresh.isVisible = true
        }
    }

}