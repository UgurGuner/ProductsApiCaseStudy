package com.eugurguner.productsapicasestudy.presentation.views.filter

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.eugurguner.productsapicasestudy.core.sortAndFilter.FilterOptions
import com.eugurguner.productsapicasestudy.core.sortAndFilter.SortOption
import com.eugurguner.productsapicasestudy.databinding.FilterBottomSheetBinding
import com.eugurguner.productsapicasestudy.presentation.adapters.brand.BrandAdapter
import com.eugurguner.productsapicasestudy.presentation.adapters.model.ModelAdapter
import com.eugurguner.productsapicasestudy.presentation.viewModels.SharedViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class FilterBottomSheetDialogFragment : BottomSheetDialogFragment() {
    private lateinit var binding: FilterBottomSheetBinding

    private var brandAdapter: BrandAdapter? = null
    private var modelAdapter: ModelAdapter? = null

    private val viewModel: SharedViewModel by activityViewModels()

    private var sortOption = SortOption.OLD_TO_NEW
    private var filterOptions = FilterOptions()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = BottomSheetDialog(requireContext(), theme)
        dialog.setOnShowListener {
            val bottomSheetDialog = it as BottomSheetDialog
            val parentLayout =
                bottomSheetDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            parentLayout?.let { pl ->
                val behaviour = BottomSheetBehavior.from(pl)
                behaviour.skipCollapsed = true
                behaviour.isDraggable = true
                behaviour.state = BottomSheetBehavior.STATE_EXPANDED
            }
        }
        return dialog
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FilterBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setOnClickListeners()
        getSortOption()
        lifecycleScope.launch {
            getFilterOptions()
        }
    }

    private fun getSortOption() {
        sortOption = viewModel.sortOption.value ?: SortOption.OLD_TO_NEW
        when (sortOption) {
            SortOption.OLD_TO_NEW -> {
                binding.rbOldToNew.isChecked = true
            }

            SortOption.NEW_TO_OLD -> {
                binding.rbNewToOld.isChecked = true
            }

            SortOption.PRICE_HIGH_TO_LOW -> {
                binding.rbPriceHighToLow.isChecked = true
            }

            SortOption.PRICE_LOW_TO_HIGH -> {
                binding.rbPriceLowToHigh.isChecked = true
            }
        }
    }

    private suspend fun getFilterOptions() {
        delay(200)
        filterOptions = viewModel.filterSettings.value ?: FilterOptions()
        setAdapters()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setAdapters() {
        brandAdapter =
            BrandAdapter(
                brandList = filterOptions.brands.toMutableList(),
                onCheckBoxClicked = { brand ->
                    brand.isSelected = !brand.isSelected
                }
            )

        modelAdapter =
            ModelAdapter(
                modelList = filterOptions.models.toMutableList(),
                onCheckBoxClicked = { model ->
                    model.isSelected = !model.isSelected
                }
            )

        binding.recyclerViewBrands.adapter = brandAdapter
        binding.recyclerViewBrands.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        binding.recyclerViewModels.adapter = modelAdapter
        binding.recyclerViewModels.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    }

    private fun setOnClickListeners() {
        binding.toolBarView.setNavigationOnClickListener { dismissAllowingStateLoss() }
        binding.btnApply.setOnClickListener { applySettings() }
    }

    private fun applySettings() {
        sortOption =
            when (binding.rdgButtonSort.checkedRadioButtonId) {
                binding.rbOldToNew.id -> {
                    SortOption.OLD_TO_NEW
                }

                binding.rbNewToOld.id -> {
                    SortOption.NEW_TO_OLD
                }

                binding.rbPriceHighToLow.id -> {
                    SortOption.PRICE_HIGH_TO_LOW
                }

                binding.rbPriceLowToHigh.id -> {
                    SortOption.PRICE_LOW_TO_HIGH
                }

                else -> {
                    SortOption.OLD_TO_NEW
                }
            }
        viewModel.updateSortOption(sortOption = sortOption)
        viewModel.updateFilterOptions(
            filterOptions =
                filterOptions
                    .copy(
                        brands = brandAdapter?.brandList ?: mutableListOf(),
                        models = modelAdapter?.modelList ?: mutableListOf()
                    )
        )
        dismissAllowingStateLoss()
    }
}