package com.eugurguner.productsapicasestudy.presentation.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.eugurguner.productsapicasestudy.core.sortAndFilter.FilterOptions
import com.eugurguner.productsapicasestudy.core.sortAndFilter.SortOption
import com.eugurguner.productsapicasestudy.domain.model.Brand
import com.eugurguner.productsapicasestudy.domain.model.Model
import com.eugurguner.productsapicasestudy.domain.model.Product

class SharedViewModel : ViewModel() {
    private val _filterSettings = MutableLiveData<FilterOptions>()
    val filterSettings: LiveData<FilterOptions> = _filterSettings

    private val _sortOption = MutableLiveData<SortOption>()
    val sortOption: LiveData<SortOption> = _sortOption

    fun setFilterOptions(productList: List<Product>) {
        val brands =
            productList
                .distinctBy { it.brand }
                .map { Brand(brandName = it.brand) }

        val models =
            productList
                .distinctBy { it.model }
                .map { Model(modelName = it.model) }

        _filterSettings.value = FilterOptions(brands = brands, models = models)
    }

    fun updateFilterOptions(filterOptions: FilterOptions) {
        _filterSettings.value = filterOptions
    }

    fun updateSortOption(sortOption: SortOption) {
        _sortOption.value = sortOption
    }
}