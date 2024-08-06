package com.eugurguner.productsapicasestudy.core

import com.eugurguner.productsapicasestudy.core.sortAndFilter.FilterOptions
import com.eugurguner.productsapicasestudy.core.sortAndFilter.SortOption
import com.eugurguner.productsapicasestudy.core.sortAndFilter.filterList
import com.eugurguner.productsapicasestudy.core.sortAndFilter.sortList
import com.eugurguner.productsapicasestudy.domain.model.Brand
import com.eugurguner.productsapicasestudy.domain.model.Model
import com.eugurguner.productsapicasestudy.domain.model.Product
import com.google.common.truth.Truth.assertThat
import kotlin.test.Test

class SortingAndCompareHelperTest {
    private val brand1 = Brand(brandName = "BrandA", isSelected = true)
    private val brand2 = Brand(brandName = "BrandB")
    private val model1 = Model(modelName = "ModelC", isSelected = true)
    private val model2 = Model(modelName = "ModelD")

    private val product1 =
        Product(
            id = "P001",
            name = "Smartphone Pro Max",
            imageUrl = "https://example.com/images/smartphone_pro_max.jpg",
            price = 1299.99,
            description = "The latest and greatest smartphone with amazing features.",
            model = model1.modelName,
            brand = brand1.brandName,
            createdAt = "2024-07-15T10:30:00Z", // Example timestamp (ISO 8601 format)
            isSaved = false,
            quantity = 1
        )

    private val product2 =
        Product(
            id = "P002",
            name = "Laptop Ultrabook",
            imageUrl = "https://example.com/images/laptop_ultrabook.jpg",
            price = 1599.99,
            description = "A powerful and lightweight laptop for productivity on the go.",
            model = model2.modelName,
            brand = brand2.brandName,
            createdAt = "2024-06-20T15:45:00Z",
            isSaved = true,
            quantity = 2
        )

    private val product3 =
        Product(
            id = "P003",
            name = "Wireless Headphones",
            imageUrl = "https://example.com/images/wireless_headphones.jpg",
            price = 199.99,
            description = "High-quality wireless headphones with noise cancellation.",
            model = model2.modelName,
            brand = brand1.brandName,
            createdAt = "2024-08-01T08:00:00Z",
            isSaved = false,
            quantity = 3
        )

    @Test
    fun `sortList sorts empty list correctly`() {
        val emptyList = emptyList<Product>()
        val result = sortList(SortOption.OLD_TO_NEW, emptyList)
        assertThat(result.isEmpty()).isTrue()
    }

    @Test
    fun `filterList filters empty list correctly`() {
        val emptyList = emptyList<Product>()
        val filterOptions = FilterOptions()
        val result = filterList(filterOptions, emptyList)
        assertThat(result.isEmpty()).isTrue()
    }

    @Test
    fun `filterList returns original list when no filters are applied`() {
        val productList = listOf(product1, product2, product3)
        val filterOptions = FilterOptions(brands = emptyList(), models = emptyList())

        val result = filterList(filterOptions, productList)

        assertThat(result).isEqualTo(productList)
    }

    @Test
    fun `filterList filters by brand correctly`() {
        val productList = listOf(product1, product2, product3)
        val filterOptions = FilterOptions(brands = listOf(brand1), models = emptyList())

        val result = filterList(filterOptions, productList)

        assertThat(result).hasSize(2)
        assertThat(result).containsExactly(product1, product3)
    }

    @Test
    fun `filterList filters by model correctly`() {
        val productList = listOf(product1, product2, product3)
        val filterOptions = FilterOptions(brands = emptyList(), models = listOf(model1))

        val result = filterList(filterOptions, productList)

        assertThat(result).hasSize(1)
        assertThat(result).containsExactly(product1)
    }

    @Test
    fun `filterList filters by both brand and model correctly`() {
        val productList = listOf(product1, product2, product3)
        val filterOptions = FilterOptions(brands = listOf(brand1), models = listOf(model1))

        val result = filterList(filterOptions, productList)

        assertThat(result).hasSize(2)
        assertThat(result).containsExactly(product1, product3)
    }
}