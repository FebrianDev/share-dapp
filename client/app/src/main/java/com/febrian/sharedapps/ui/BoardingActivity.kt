package com.febrian.sharedapps.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.febrian.sharedapps.databinding.ActivityBoardingBinding

class BoardingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBoardingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBoardingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewAdapter = ViewPagerAdapter(this, this)
        binding.viewPager.adapter = viewAdapter
        binding.indicator.attachTo(binding.viewPager)

    }
}