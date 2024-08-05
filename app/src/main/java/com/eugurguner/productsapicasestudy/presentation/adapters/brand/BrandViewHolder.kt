package com.eugurguner.productsapicasestudy.presentation.adapters.brand

import androidx.recyclerview.widget.RecyclerView
import com.eugurguner.productsapicasestudy.databinding.BrandItemBinding
import com.eugurguner.productsapicasestudy.domain.model.Brand

class BrandViewHolder(
    val binding: BrandItemBinding
) : RecyclerView.ViewHolder(binding.root) {
    fun bindView(brand: Brand) {
        binding.brandCheckBox.text = brand.brandName
        binding.brandCheckBox.isChecked = brand.isSelected
    }
}