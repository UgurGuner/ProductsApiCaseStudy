package com.eugurguner.productsapicasestudy.presentation.adapters.product

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.eugurguner.productsapicasestudy.R
import com.eugurguner.productsapicasestudy.databinding.ProductItemBinding
import com.eugurguner.productsapicasestudy.domain.model.Product

class ProductViewHolder(
    val binding: ProductItemBinding
) : RecyclerView.ViewHolder(binding.root) {
    fun bindView(product: Product) {
        if (product.imageUrl.isNotBlank()) {
            Glide.with(binding.root).load(product.imageUrl).into(binding.imgProduct)
        }
        binding.txtProductModel.text = product.model
        binding.txtProductPrice.text = product.price.toString()
        if (product.isSaved) {
            binding.imgSave.setImageResource(R.drawable.saved_icon)
        } else {
            binding.imgSave.setImageResource(R.drawable.unsaved_icon)
        }
    }
}