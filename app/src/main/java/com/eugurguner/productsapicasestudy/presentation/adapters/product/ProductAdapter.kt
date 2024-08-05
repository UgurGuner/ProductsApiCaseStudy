package com.eugurguner.productsapicasestudy.presentation.adapters.product

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.eugurguner.productsapicasestudy.databinding.ProductItemBinding
import com.eugurguner.productsapicasestudy.domain.model.Product

class ProductAdapter(
    var productList: MutableList<Product>,
    private val onProductClicked: (Product) -> Unit,
    private val onSaveClicked: (Product) -> Unit,
    private val onAddToCartClicked: (Product) -> Unit
) : RecyclerView.Adapter<ProductViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding = ProductItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductViewHolder(binding = binding)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bindView(product = productList[position])

        holder.itemView.setOnClickListener {
            onProductClicked.invoke(productList[position])
        }

        holder.binding.btnSave.setOnClickListener {
            onSaveClicked.invoke(productList[position])
        }

        holder.binding.btnAddToCart.setOnClickListener {
            onAddToCartClicked.invoke(productList[position])
        }
    }

    override fun getItemCount(): Int = productList.size
}