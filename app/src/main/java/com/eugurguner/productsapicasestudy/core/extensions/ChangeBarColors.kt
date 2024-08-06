package com.eugurguner.productsapicasestudy.core.extensions

import android.app.Activity
import android.os.Build
import androidx.core.content.ContextCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.eugurguner.productsapicasestudy.R

fun Activity.changeStatusBarColor(color: Int) {
    window?.let {
        val wic = WindowInsetsControllerCompat(it, it.decorView)

        wic.isAppearanceLightStatusBars = true

        wic.isAppearanceLightNavigationBars = true
    }

    window.statusBarColor = ContextCompat.getColor(applicationContext, color)
}

fun Activity.changeNavigationBarColor(color: Int) {
    window?.let {
        val wic = WindowInsetsControllerCompat(it, it.decorView)

        wic.isAppearanceLightStatusBars = true

        wic.isAppearanceLightNavigationBars = true
    }

    window.navigationBarColor = ContextCompat.getColor(applicationContext, color)

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
        window.navigationBarDividerColor = ContextCompat.getColor(applicationContext, R.color.soft_gray)
    }
}