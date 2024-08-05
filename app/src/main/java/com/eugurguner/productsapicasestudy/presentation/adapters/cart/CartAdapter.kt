package com.eugurguner.productsapicasestudy.presentation.adapters.cart

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.eugurguner.productsapicasestudy.databinding.CartItemBinding
import com.eugurguner.productsapicasestudy.domain.model.Product

class CartAdapter(
    var cartProductList: MutableList<Product>,
    private val onIncrease: (Product) -> Unit,
    private val onDecrease: (Product) -> Unit
) : RecyclerView.Adapter<CartViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val binding = CartItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CartViewHolder(binding = binding)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        holder.bindView(product = cartProductList[position])

        holder.binding.btnDecrease.setOnClickListener {
            onDecrease.invoke(cartProductList[position])
        }

        holder.binding.btnIncrease.setOnClickListener {
            onIncrease.invoke(cartProductList[position])
        }
    }

    override fun getItemCount(): Int = cartProductList.size
}