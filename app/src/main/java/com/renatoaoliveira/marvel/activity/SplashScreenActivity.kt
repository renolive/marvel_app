package com.renatoaoliveira.marvel.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.WindowManager
import android.widget.Toast
import android.widget.Toolbar
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import com.renatoaoliveira.marvel.R
import kotlinx.coroutines.*

class SplashScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_screen)

        setTimerAndOpenHomeActivity()
    }

    private fun setTimerAndOpenHomeActivity() {
        CoroutineScope(Dispatchers.Main).launch {
            delay(SPLASH_SCREEN_TIME)
            Toast.makeText(this@SplashScreenActivity, "hahahaahahahah", Toast.LENGTH_SHORT).show()
//        startActivity(
//            Intent(this, HomeActivity::class.java).apply {
//                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
//            }
//        )
        }
    }

    companion object {
        const val SPLASH_SCREEN_TIME = 2000L
    }
}