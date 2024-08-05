package com.eugurguner.productsapicasestudy.core.sortAndFilter

import com.eugurguner.productsapicasestudy.domain.model.Brand
import com.eugurguner.productsapicasestudy.domain.model.Model

data class FilterOptions(
    val brands: List<Brand> = emptyList(),
    val models: List<Model> = emptyList()
)