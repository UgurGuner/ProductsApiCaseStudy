package com.eugurguner.productsapicasestudy.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.eugurguner.productsapicasestudy.data.model.CartProductDTO

@Dao
interface CartDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCartItem(cartItem: CartProductDTO)

    @Query("DELETE FROM cart_product WHERE id = :productId")
    suspend fun deleteCartItem(productId: String)

    @Query("SELECT * FROM cart_product")
    fun getAllCartItems(): List<CartProductDTO>
}