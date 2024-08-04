package com.eugurguner.productsapicasestudy.data.repository

import com.eugurguner.productsapicasestudy.data.dataSource.ProductDataSource
import com.eugurguner.productsapicasestudy.data.local.dao.CartDao
import com.eugurguner.productsapicasestudy.data.local.dao.ProductDao
import com.eugurguner.productsapicasestudy.data.mapper.toCartDTO
import com.eugurguner.productsapicasestudy.data.mapper.toDTO
import com.eugurguner.productsapicasestudy.data.mapper.toDomainModel
import com.eugurguner.productsapicasestudy.domain.model.Product
import com.eugurguner.productsapicasestudy.domain.repository.ProductRepository

class ProductRepositoryImpl(
    private val productDao: ProductDao,
    private val cartDao: CartDao,
    private val productDataSource: ProductDataSource
) : ProductRepository {
    override suspend fun fetchProducts(): List<Product> = productDataSource.fetchProducts().map { it.toDomainModel() }

    override suspend fun getFavoriteProducts(): List<Product> = productDao.getAll().map { it.toDomainModel() }

    override suspend fun getCartProducts(): List<Product> = cartDao.getAllCartItems().map { it.toDomainModel() }

    override suspend fun saveProduct(product: Product) {
        productDao.insert(product = product.toDTO())
    }

    override suspend fun addProductToCart(product: Product) {
        cartDao.insertCartItem(cartItem = product.toCartDTO())
    }

    override suspend fun removeProduct(productId: String) {
        productDao.deleteById(productId = productId)
    }

    override suspend fun removeProductFromCart(productId: String) {
        cartDao.deleteCartItem(productId = productId)
    }
}