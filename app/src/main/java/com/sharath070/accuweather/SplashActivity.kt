package com.sharath070.accuweather

import android.app.ActivityOptions
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Window
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)


        changeStatusBarColor(R.color.system_bars_for_splash_screen)
        changeNavigationBarColor(R.color.system_bars_for_splash_screen)

        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, MainActivity::class.java)
            val options = ActivityOptions.makeCustomAnimation(
                this,
                androidx.appcompat.R.anim.abc_fade_in,
                com.google.android.material.R.anim.abc_fade_out
            )
            startActivity(intent, options.toBundle())
            finish()
        }, 1300)

    }


    private fun changeStatusBarColor(@ColorRes colorRes: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window: Window = window
            val color = ContextCompat.getColor(this, colorRes)
            window.statusBarColor = color
        }
    }

    private fun changeNavigationBarColor(@ColorRes colorRes: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window: Window = window
            val color = ContextCompat.getColor(this, colorRes)
            window.navigationBarColor = color
        }
    }
}