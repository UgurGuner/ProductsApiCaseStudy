package com.eugurguner.productsapicasestudy.core.sortAndFilter

import com.eugurguner.productsapicasestudy.domain.model.Product

fun sortList(sortOption: SortOption, productList: List<Product>): List<Product> {
    val sortedProductList =
        when (sortOption) {
            SortOption.OLD_TO_NEW -> {
                productList.sortedBy { it.createdAt }
            }

            SortOption.NEW_TO_OLD -> {
                productList.sortedByDescending { it.createdAt }
            }

            SortOption.PRICE_HIGH_TO_LOW -> {
                productList.sortedByDescending { it.price }
            }

            SortOption.PRICE_LOW_TO_HIGH -> {
                productList.sortedBy { it.price }
            }
        }
    return sortedProductList
}

fun filterList(filterOptions: FilterOptions, productList: List<Product>): List<Product> {
    val brands = filterOptions.brands.filter { it.isSelected }.map { it.brandName }
    val models = filterOptions.models.filter { it.isSelected }.map { it.modelName }
    val filteredProducts = mutableListOf<Product>()
    productList.forEach {
        if (brands.contains(it.brand)) {
            filteredProducts.add(it)
        } else if (models.contains(it.model)) {
            filteredProducts.add(it)
        }
    }
    return filteredProducts
}