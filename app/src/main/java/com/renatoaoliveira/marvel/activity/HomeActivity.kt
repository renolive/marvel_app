package com.renatoaoliveira.marvel.activity

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.renatoaoliveira.character.presentation.ui.FavoriteCharactersGridFragment
import com.renatoaoliveira.character.presentation.ui.HomeCharactersGridFragment
import com.renatoaoliveira.marvel.R
import com.renatoaoliveira.marvel.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {

    private lateinit var homeBinding: ActivityHomeBinding

    private val homeCharactersGridFragment = HomeCharactersGridFragment()
    private val favoriteCharactersGridFragment = FavoriteCharactersGridFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        homeBinding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(homeBinding.root)

        loadHomeFragment()
        configureViews()
    }

    private fun loadHomeFragment() {
        loadFragment(homeCharactersGridFragment)
    }

    private fun loadFavoriteFragment() {
        loadFragment(favoriteCharactersGridFragment)
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(homeBinding.fragmentContainerView.id, fragment)
            commit()
        }
    }

    private fun configureViews() = with(homeBinding) {
        bottomNavBar.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.home_item -> {
                    Toast.makeText(
                        this@HomeActivity,
                        "Selected Home",
                        Toast.LENGTH_SHORT
                    ).show()
                    loadHomeFragment()
                }
                R.id.favorites_item -> {
                    Toast.makeText(
                        this@HomeActivity,
                        "Selected Favorite",
                        Toast.LENGTH_SHORT
                    ).show()
                    loadFavoriteFragment()
                }
            }
            true
        }
    }

}