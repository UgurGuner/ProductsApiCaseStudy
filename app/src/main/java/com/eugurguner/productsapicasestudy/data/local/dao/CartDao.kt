package com.eugurguner.productsapicasestudy.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.eugurguner.productsapicasestudy.data.model.CartProductDTO

@Dao
interface CartDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCartItem(cartItem: CartProductDTO)

    @Update
    suspend fun updateCartItemQuantity(cartItem: CartProductDTO)

    @Query("DELETE FROM cart_product WHERE id = :productId")
    suspend fun deleteCartItem(productId: String)

    @Query("SELECT * FROM cart_product")
    suspend fun getAllCartItems(): List<CartProductDTO>

    @Query("SELECT * FROM cart_product WHERE id = :productId")
    suspend fun getCartItemById(productId: String): CartProductDTO?
}