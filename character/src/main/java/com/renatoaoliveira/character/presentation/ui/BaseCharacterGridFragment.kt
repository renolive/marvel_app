package com.renatoaoliveira.character.presentation.ui

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.renatoaoliveira.character.R
import com.renatoaoliveira.character.databinding.BottomStatusLoadBinding
import com.renatoaoliveira.character.databinding.FragmentCharacterListBinding
import com.renatoaoliveira.common.connection.checkConnectivity

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