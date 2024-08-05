package com.eugurguner.productsapicasestudy.core

sealed class AppEvents {
    data object None : AppEvents()
    data object OnCartBadgeUpdate : AppEvents()
    data object OnFavoriteBadgeUpdate : AppEvents()
}