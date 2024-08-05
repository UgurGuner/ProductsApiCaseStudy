package com.eugurguner.productsapicasestudy.presentation.adapters.brand

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.eugurguner.productsapicasestudy.databinding.BrandItemBinding
import com.eugurguner.productsapicasestudy.domain.model.Brand

class BrandAdapter(
    var brandList: MutableList<Brand>,
    private val onCheckBoxClicked: (Brand) -> Unit
) : RecyclerView.Adapter<BrandViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BrandViewHolder {
        val binding = BrandItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BrandViewHolder(binding = binding)
    }

    override fun onBindViewHolder(holder: BrandViewHolder, position: Int) {
        holder.bindView(brand = brandList[position])
        holder.binding.brandCheckBox.setOnCheckedChangeListener { _, _ ->
            onCheckBoxClicked.invoke(brandList[position])
        }
    }

    override fun getItemCount(): Int = brandList.size
}