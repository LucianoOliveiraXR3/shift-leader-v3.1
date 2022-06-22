package br.com.crearesistemas.shift_leader.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import br.com.crearesistemas.shift_leader.R

class SplashScreem : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screem)
        supportActionBar?.hide()
    }
}