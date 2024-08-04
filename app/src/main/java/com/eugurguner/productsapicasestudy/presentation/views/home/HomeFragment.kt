package com.eugurguner.productsapicasestudy.presentation.views.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.eugurguner.productsapicasestudy.core.UIState
import com.eugurguner.productsapicasestudy.databinding.FragmentHomeBinding
import com.eugurguner.productsapicasestudy.domain.model.Product
import com.eugurguner.productsapicasestudy.presentation.adapters.product.ProductAdapter
import com.eugurguner.productsapicasestudy.presentation.viewModels.HomeFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private var adapter: ProductAdapter? = null

    private val viewModel: HomeFragmentViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentHomeBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = binding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.onBackPressedDispatcher?.addCallback(onBackPressedCallback)
        setUpAdapter()
    }

    private fun setUpAdapter() {
        adapter =
            ProductAdapter(
                productList = mutableListOf(),
                onProductClicked = {},
                onSaveClicked = {}
            )

        binding.recyclerView.layoutManager = GridLayoutManager(context, 2, LinearLayoutManager.VERTICAL, false)
        binding.recyclerView.adapter = adapter

        viewModelListener()
    }

    private fun viewModelListener() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collect { uiState ->
                handleUiState(uiState)
            }
        }
        viewModel.fetchProducts()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun handleUiState(uiState: UIState<List<Product>>) {
        when (uiState) {
            is UIState.Loading -> {
                // Show loading indicator
            }

            is UIState.Success -> {
                val data = uiState.data
                adapter?.productList = data
                adapter?.notifyDataSetChanged()
                // Update UI with the product list (uiState.data)
            }

            is UIState.Error -> {
                // Show error message (uiState.message)
            }

            is UIState.Empty -> {
                // Display an empty state message
            }
        }
    }

    private val onBackPressedCallback =
        object : OnBackPressedCallback(enabled = true) {
            override fun handleOnBackPressed() {
                activity?.finishAffinity()
            }
        }
}