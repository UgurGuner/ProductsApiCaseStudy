package com.eugurguner.productsapicasestudy.presentation.adapters.model

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.eugurguner.productsapicasestudy.databinding.ModelItemBinding
import com.eugurguner.productsapicasestudy.domain.model.Model

class ModelAdapter(
    var modelList: MutableList<Model>,
    private val onCheckBoxClicked: (Model) -> Unit
) : RecyclerView.Adapter<ModelViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ModelViewHolder {
        val binding = ModelItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ModelViewHolder(binding = binding)
    }

    override fun onBindViewHolder(holder: ModelViewHolder, position: Int) {
        holder.bindView(model = modelList[position])
        holder.binding.modelCheckBox.setOnCheckedChangeListener { _, _ ->
            onCheckBoxClicked.invoke(modelList[position])
        }
    }

    override fun getItemCount(): Int = modelList.size
}