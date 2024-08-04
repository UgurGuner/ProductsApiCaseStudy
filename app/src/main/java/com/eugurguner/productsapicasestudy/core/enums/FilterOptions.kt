package com.eugurguner.productsapicasestudy.core.enums

data class FilterOptions(
    val selectedBrands: List<String> = emptyList(),
    val selectedModels: List<String> = emptyList()
)