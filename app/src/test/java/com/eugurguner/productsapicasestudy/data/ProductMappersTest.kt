package com.eugurguner.productsapicasestudy.data

import com.eugurguner.productsapicasestudy.data.mapper.toCartDTO
import com.eugurguner.productsapicasestudy.data.mapper.toDTO
import com.eugurguner.productsapicasestudy.data.mapper.toDomainModel
import com.eugurguner.productsapicasestudy.data.model.CartProductDTO
import com.eugurguner.productsapicasestudy.data.model.ProductDTO
import com.eugurguner.productsapicasestudy.domain.model.Product
import com.google.common.truth.Truth.assertThat
import kotlin.test.Test

class ProductMappersTest {
    private val productDTO =
        ProductDTO(
            id = "P001",
            name = "Product A",
            imageUrl = "image_url",
            price = "10.0",
            description = "Description",
            model = "Model A",
            brand = "Brand A",
            createdAt = "2024-08-05T14:52:00Z"
        )
    private val product =
        Product(
            id = "P001",
            name = "Product A",
            imageUrl = "image_url",
            price = 10.0,
            description = "Description",
            model = "Model A",
            brand = "Brand A",
            createdAt = "2024-08-05T14:52:00Z"
        )
    private val cartProductDTO =
        CartProductDTO(
            id = "P001",
            name = "Product A",
            imageUrl = "image_url",
            price = "10.0",
            description = "Description",
            model = "Model A",
            brand = "Brand A",
            createdAt = "2024-08-05T14:52:00Z",
            quantity = 2
        )

    @Test
    fun `ProductDTO toDomainModel converts correctly`() {
        val result = productDTO.toDomainModel()

        assertThat(result.id).isEqualTo(productDTO.id)
        assertThat(result.name).isEqualTo(productDTO.name)
        assertThat(result.imageUrl).isEqualTo(productDTO.imageUrl)
        assertThat(result.price).isEqualTo(productDTO.price.toDouble())
        assertThat(result.description).isEqualTo(productDTO.description)
        assertThat(result.model).isEqualTo(productDTO.model)
        assertThat(result.brand).isEqualTo(productDTO.brand)
        assertThat(result.createdAt).isEqualTo(productDTO.createdAt)
    }

    @Test
    fun `Product toDTO converts correctly`() {
        val result = product.toDTO()
        assertThat(result).isEqualTo(productDTO)
    }

    @Test
    fun `CartProductDTO toDomainModel converts correctly`() {
        val result = cartProductDTO.toDomainModel()

        assertThat(result).isEqualTo(product.copy(quantity = 2))
    }

    @Test
    fun `Product toCartDTO converts correctly`() {
        val result = product.toCartDTO()
        assertThat(result).isEqualTo(cartProductDTO.copy(quantity = 1))
    }

    @Test
    fun `ProductDTO toDomainModel toDTO round trip`() {
        val domainModel = productDTO.toDomainModel()
        val resultDTO = domainModel.toDTO()

        assertThat(resultDTO).isEqualTo(productDTO)
    }

    @Test
    fun `Product toCartDTO toDomainModel round trip`() {
        val cartDTO = product.toCartDTO()
        val resultProduct = cartDTO.toDomainModel()

        assertThat(resultProduct).isEqualTo(product)
    }
}