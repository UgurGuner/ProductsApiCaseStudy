package com.eugurguner.productsapicasestudy.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.eugurguner.productsapicasestudy.data.model.ProductDTO

@Dao
interface ProductDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(product: ProductDTO)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(products: List<ProductDTO>)

    @Update
    suspend fun update(product: ProductDTO)

    @Query("DELETE FROM product WHERE id = :productId")
    suspend fun deleteById(productId: String)

    @Query("SELECT * FROM product")
    suspend fun getAll(): List<ProductDTO>

    @Query("SELECT * FROM product WHERE id = :productId")
    suspend fun getById(productId: String): ProductDTO?
}