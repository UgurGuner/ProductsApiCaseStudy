package com.eugurguner.productsapicasestudy.presentation.adapters.model

import androidx.recyclerview.widget.RecyclerView
import com.eugurguner.productsapicasestudy.databinding.ModelItemBinding
import com.eugurguner.productsapicasestudy.domain.model.Model

class ModelViewHolder(
    val binding: ModelItemBinding
) : RecyclerView.ViewHolder(binding.root) {
    fun bindView(model: Model) {
        binding.modelCheckBox.text = model.modelName
        binding.modelCheckBox.isChecked = model.isSelected
    }
}