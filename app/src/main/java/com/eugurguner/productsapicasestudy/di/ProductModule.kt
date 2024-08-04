package com.eugurguner.productsapicasestudy.di

import com.eugurguner.productsapicasestudy.data.dataSource.ProductDataSource
import com.eugurguner.productsapicasestudy.data.local.dao.CartDao
import com.eugurguner.productsapicasestudy.data.local.dao.ProductDao
import com.eugurguner.productsapicasestudy.data.local.db.AppDatabase
import com.eugurguner.productsapicasestudy.data.network.AppNetworkService
import com.eugurguner.productsapicasestudy.data.repository.ProductRepositoryImpl
import com.eugurguner.productsapicasestudy.domain.repository.ProductRepository
import com.eugurguner.productsapicasestudy.domain.useCase.FetchProductsUseCase
import com.eugurguner.productsapicasestudy.domain.useCase.GetSavedProductsUseCase
import com.eugurguner.productsapicasestudy.domain.useCase.ProductUseCases
import com.eugurguner.productsapicasestudy.domain.useCase.RemoveProductUseCase
import com.eugurguner.productsapicasestudy.domain.useCase.SaveProductUseCase
import com.eugurguner.productsapicasestudy.domain.useCase.cart.AddProductToCartUseCase
import com.eugurguner.productsapicasestudy.domain.useCase.cart.CartProductUseCases
import com.eugurguner.productsapicasestudy.domain.useCase.cart.GetCartProductsUseCase
import com.eugurguner.productsapicasestudy.domain.useCase.cart.RemoveProductFromCartUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ProductModule {
    @Provides
    fun provideProductDao(database: AppDatabase): ProductDao = database.productDao()

    @Provides
    fun provideCartDao(database: AppDatabase): CartDao = database.cartDao()

    @Provides
    @Singleton
    fun provideProductDataSource(
        appNetworkService: AppNetworkService
    ): ProductDataSource = ProductDataSource(appNetworkService = appNetworkService)

    @Provides
    @Singleton
    fun provideProductRepository(
        productDao: ProductDao,
        cartDao: CartDao,
        productDataSource: ProductDataSource
    ): ProductRepository =
        ProductRepositoryImpl(
            productDao = productDao,
            cartDao = cartDao,
            productDataSource = productDataSource
        )

    @Provides
    @Singleton
    fun provideProductUseCases(
        productRepository: ProductRepository
    ): ProductUseCases =
        ProductUseCases(
            fetchProductsUseCase = FetchProductsUseCase(productRepository = productRepository),
            getSavedProductsUseCase = GetSavedProductsUseCase(productRepository = productRepository),
            saveProductUseCase = SaveProductUseCase(productRepository = productRepository),
            removeProductUseCase = RemoveProductUseCase(productRepository = productRepository)
        )

    @Provides
    @Singleton
    fun provideCartProductUseCases(
        productRepository: ProductRepository
    ): CartProductUseCases =
        CartProductUseCases(
            getCartProductsUseCase = GetCartProductsUseCase(productRepository = productRepository),
            addProductToCartUseCase = AddProductToCartUseCase(productRepository = productRepository),
            removeProductFromCartUseCase = RemoveProductFromCartUseCase(productRepository = productRepository)
        )
}