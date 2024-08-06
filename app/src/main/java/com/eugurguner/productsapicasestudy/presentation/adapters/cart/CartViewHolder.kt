package com.eugurguner.productsapicasestudy.presentation.adapters.cart

import android.annotation.SuppressLint
import androidx.recyclerview.widget.RecyclerView
import com.eugurguner.productsapicasestudy.core.StaticVariables
import com.eugurguner.productsapicasestudy.core.extensions.formatPrice
import com.eugurguner.productsapicasestudy.databinding.CartItemBinding
import com.eugurguner.productsapicasestudy.domain.model.Product

class CartViewHolder(
    val binding: CartItemBinding
) : RecyclerView.ViewHolder(binding.root) {
    @SuppressLint("SetTextI18n")
    fun bindView(product: Product) {
        val totalPrice = product.price * product.quantity
        binding.txtProductName.text = product.name
        binding.txtTotalPrice.text = "${totalPrice.formatPrice()}${StaticVariables.CURRENCY_SYMBOL}"
        binding.txtProductQuantity.text = product.quantity.toString()
    }
}