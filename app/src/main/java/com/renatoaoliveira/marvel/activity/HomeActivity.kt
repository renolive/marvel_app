package com.renatoaoliveira.marvel.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.renatoaoliveira.character.presentation.ui.HomeCharactersGridFragment
import com.renatoaoliveira.character.presentation.viewmodel.CharacterFavoritesViewModel
import com.renatoaoliveira.character.presentation.viewmodel.CharactersListViewModel
import com.renatoaoliveira.marvel.R
import com.renatoaoliveira.marvel.databinding.ActivityHomeBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeActivity : AppCompatActivity() {

    private lateinit var homeBinding: ActivityHomeBinding

    private val homeCharactersGridFragment = HomeCharactersGridFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        homeBinding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(homeBinding.root)

        loadHomeFragment()
    }

    private fun loadHomeFragment() {
        loadFragment(homeCharactersGridFragment)
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(homeBinding.fragmentContainerView.id, fragment)
            if (supportFragmentManager.backStackEntryCount > 0) {
                addToBackStack("")
            }
            commit()
        }
    }
}