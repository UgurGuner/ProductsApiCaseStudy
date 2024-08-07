package com.eugurguner.productsapicasestudy.presentation.activities

import android.os.Bundle
import androidx.activity.addCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.eugurguner.productsapicasestudy.R
import com.eugurguner.productsapicasestudy.core.extensions.changeNavigationBarColor
import com.eugurguner.productsapicasestudy.core.extensions.changeStatusBarColor
import com.eugurguner.productsapicasestudy.databinding.ActivityMainBinding
import com.eugurguner.productsapicasestudy.presentation.viewModels.MainActivityViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainActivityViewModel by viewModels()
    private var selectedTab = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        changeStatusBarColor(R.color.accentColor)
        changeNavigationBarColor(R.color.white)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setBottomNavigationBar()
    }

    private fun setBottomNavigationBar() {
        val navController = findNavController(R.id.navHostFragment)
        binding.bottomNavigationView.setupWithNavController(navController)
        this.selectedTab = binding.bottomNavigationView.selectedItemId
        setBadgeObserver()
        binding.bottomNavigationView.setOnItemSelectedListener { item ->

            if (selectedTab == item.itemId) return@setOnItemSelectedListener false

            this.selectedTab = item.itemId

            when (item.itemId) {
                R.id.destination_item1 -> {
                    navController.popBackStack(destinationId = R.id.homeFragment, inclusive = false)
                    true
                }

                R.id.destination_item2 -> {
                    navController.navigate(R.id.cartFragment)
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
        addOnBackPressedDispatcher(navController)
    }

    private fun addOnBackPressedDispatcher(navController: NavController) {
        onBackPressedDispatcher.addCallback(this) {
            if (
                navController.currentDestination?.id == R.id.homeFragment ||
                navController.currentDestination?.id == R.id.cartFragment ||
                navController.currentDestination?.id == R.id.favoritesFragment ||
                navController.currentDestination?.id == R.id.userFragment
            ) {
                finishAffinity()
            } else {
                if (!navController.navigateUp()) {
                    finishAffinity()
                }
            }
        }
    }

    private fun setBadgeObserver() {
        viewModel.favoriteBadgeCount.observe(this) { count ->
            updateFavoriteBadge(count = count)
        }
        viewModel.cartBadgeCount.observe(this) { count ->
            updateCartBadge(count = count)
        }
        viewModel.updateFavoriteBadgeCount()
        viewModel.updateCartBadgeCount()
    }

    private fun updateFavoriteBadge(count: Int) {
        val badge = binding.bottomNavigationView.getOrCreateBadge(R.id.destination_item3)
        badge.backgroundColor = ContextCompat.getColor(this, R.color.badgeRed)
        badge.badgeTextColor = ContextCompat.getColor(this, R.color.white)
        badge.isVisible = count > 0
        badge.number = count
    }

    private fun updateCartBadge(count: Int) {
        val badge = binding.bottomNavigationView.getOrCreateBadge(R.id.destination_item2)
        badge.backgroundColor = ContextCompat.getColor(this, R.color.badgeRed)
        badge.badgeTextColor = ContextCompat.getColor(this, R.color.white)
        badge.isVisible = count > 0
        badge.number = count
    }
}