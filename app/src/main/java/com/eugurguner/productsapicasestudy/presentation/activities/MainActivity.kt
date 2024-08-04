package com.eugurguner.productsapicasestudy.presentation.activities

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.eugurguner.productsapicasestudy.R
import com.eugurguner.productsapicasestudy.core.extensions.changeNavigationBarColor
import com.eugurguner.productsapicasestudy.core.extensions.changeStatusBarColor
import com.eugurguner.productsapicasestudy.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var selectedTab = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        changeStatusBarColor(R.color.accentColor)
        changeNavigationBarColor(R.color.white)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        onBackPressedDispatcher.addCallback(onBackPressedCallback)
        setBottomNavigationBar()
    }

    private fun setBottomNavigationBar() {
        val navController = findNavController(R.id.navHostFragment)
        binding.bottomNavigationView.setupWithNavController(navController)
        this.selectedTab = binding.bottomNavigationView.selectedItemId
        setBadgeForCart()
        binding.bottomNavigationView.setOnItemSelectedListener { item ->

            if (selectedTab == item.itemId) return@setOnItemSelectedListener false

            this.selectedTab = item.itemId

            when (item.itemId) {
                R.id.destination_item1 -> {
                    navController.navigate(R.id.homeFragment)
                    true
                }

                R.id.destination_item2 -> {
                    navController.navigate(R.id.basketFragment)
                    true
                }

                R.id.destination_item3 -> {
                    navController.navigate(R.id.favoritesFragment)
                    true
                }

                R.id.destination_item4 -> {
                    navController.navigate(R.id.userFragment)
                    true
                }

                else -> {
                    false
                }
            }
        }
    }

    private fun setBadgeForCart() {
        val badge = binding.bottomNavigationView.getOrCreateBadge(R.id.destination_item2)
        badge.backgroundColor = ContextCompat.getColor(this, R.color.badgeRed)
        badge.badgeTextColor = ContextCompat.getColor(this, R.color.white)
        badge.isVisible = true
        badge.number = 10
    }

    private val onBackPressedCallback =
        object : OnBackPressedCallback(enabled = true) {
            override fun handleOnBackPressed() {
                finishAffinity()
            }
        }
}