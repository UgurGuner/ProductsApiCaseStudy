package com.eugurguner.productsapicasestudy.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.eugurguner.productsapicasestudy.data.local.dao.CartDao
import com.eugurguner.productsapicasestudy.data.local.dao.ProductDao
import com.eugurguner.productsapicasestudy.data.model.CartProductDTO
import com.eugurguner.productsapicasestudy.data.model.ProductDTO

@Database(entities = [ProductDTO::class, CartProductDTO::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun productDao(): ProductDao
    abstract fun cartDao(): CartDao
}