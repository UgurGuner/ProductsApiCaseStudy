package com.eugurguner.productsapicasestudy.data.repository

import com.eugurguner.productsapicasestudy.data.dataSource.ProductDataSource
import com.eugurguner.productsapicasestudy.data.local.dao.ProductDao
import com.eugurguner.productsapicasestudy.data.mapper.toDTO
import com.eugurguner.productsapicasestudy.data.mapper.toDomainModel
import com.eugurguner.productsapicasestudy.domain.model.Product
import com.eugurguner.productsapicasestudy.domain.repository.ProductRepository

class ProductRepositoryImpl(
    private val productDao: ProductDao,
    private val productDataSource: ProductDataSource
) : ProductRepository {
    override suspend fun fetchProducts(): List<Product> = productDataSource.fetchProducts().map { it.toDomainModel() }

    override suspend fun getFavoriteProducts(): List<Product> = productDao.getAll().map { it.toDomainModel() }

    override suspend fun saveProduct(product: Product) {
        productDao.insert(product = product.toDTO())
    }

    override suspend fun removeProduct(productId: String) {
        productDao.deleteById(productId = productId)
    }
}