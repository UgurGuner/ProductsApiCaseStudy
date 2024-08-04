package com.eugurguner.productsapicasestudy.di

import com.eugurguner.productsapicasestudy.data.dataSource.ProductDataSource
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
    @Singleton
    fun provideProductDataSource(
        appNetworkService: AppNetworkService
    ): ProductDataSource = ProductDataSource(appNetworkService = appNetworkService)

    @Provides
    @Singleton
    fun provideProductRepository(
        productDao: ProductDao,
        productDataSource: ProductDataSource
    ): ProductRepository = ProductRepositoryImpl(productDao = productDao, productDataSource = productDataSource)

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
}