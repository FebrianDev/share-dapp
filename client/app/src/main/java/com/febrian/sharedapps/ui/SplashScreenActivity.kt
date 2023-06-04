package com.febrian.sharedapps.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import com.febrian.sharedapps.AuthActivity
import com.febrian.sharedapps.R
import com.febrian.sharedapps.databinding.ActivitySplashScreenBinding
import com.febrian.sharedapps.utils.Constant.ACCOUNT
import com.febrian.sharedapps.utils.Constant.KEY_LOG
import com.febrian.sharedapps.utils.PreferenceManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SplashScreenActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashScreenBinding

    @Inject
    lateinit var preferenceManager: PreferenceManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val value = preferenceManager.getString(KEY_LOG, "")
        Handler(Looper.getMainLooper()).postDelayed({
            if (value == KEY_LOG) {
                val intent = Intent(applicationContext, MainActivity::class.java)
                startActivity(intent)
                finish()
            }  else {
                val intent = Intent(applicationContext, BoardingActivity::class.java)
                startActivity(intent)
                finish()
            }
        }, 1500)

        val animLogo = AnimationUtils.loadAnimation(applicationContext, R.anim.logo_anim)
        val animText = AnimationUtils.loadAnimation(applicationContext, R.anim.text_logo)
        binding.imgLogo.startAnimation(animLogo)
        binding.text.startAnimation(animText)
    }
}