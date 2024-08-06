package com.eugurguner.productsapicasestudy.core

import com.eugurguner.productsapicasestudy.core.sortAndFilter.FilterOptions
import com.eugurguner.productsapicasestudy.domain.model.Brand
import com.eugurguner.productsapicasestudy.domain.model.Model
import com.google.common.truth.Truth
import kotlin.test.Test

class FilterOptionsTest {
    private val brand1 = Brand(brandName = "BrandA")
    private val brand2 = Brand(brandName = "BrandB")
    private val model1 = Model(modelName = "ModelC")

    @Test
    fun `FilterOptions initializes with default values`() {
        val options = FilterOptions()
        Truth.assertThat(options.brands.isEmpty()).isTrue()
        Truth.assertThat(options.models.isEmpty()).isTrue()
    }

    @Test
    fun `FilterOptions initializes with custom values`() {
        val options = FilterOptions(listOf(brand1, brand2), listOf(model1))
        Truth.assertThat(options.brands.size).isEqualTo(2)
        Truth.assertThat(options.models.size).isEqualTo(1)
        Truth.assertThat(options.brands.contains(brand1)).isTrue()
        Truth.assertThat(options.brands.contains(brand2)).isTrue()
        Truth.assertThat(options.models.contains(model1)).isTrue()
    }

    @Test
    fun `copy creates a new object with modifications`() {
        val original = FilterOptions(listOf(brand1), listOf(model1))
        val copy = original.copy(brands = listOf(brand2), models = emptyList())

        Truth.assertThat(original).isNotEqualTo(copy)
        Truth.assertThat(listOf(brand2)).isEqualTo(copy.brands)
        Truth.assertThat(copy.models.isEmpty()).isTrue()
    }
}