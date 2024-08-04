package com.eugurguner.productsapicasestudy.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.eugurguner.productsapicasestudy.data.local.dao.ProductDao
import com.eugurguner.productsapicasestudy.data.model.ProductDTO

@Database(entities = [ProductDTO::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun productDao(): ProductDao
}