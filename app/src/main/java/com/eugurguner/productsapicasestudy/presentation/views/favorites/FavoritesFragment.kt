package com.eugurguner.productsapicasestudy.presentation.views.favorites

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.eugurguner.productsapicasestudy.core.UIState
import com.eugurguner.productsapicasestudy.databinding.FragmentFavoritesBinding
import com.eugurguner.productsapicasestudy.domain.model.Product
import com.eugurguner.productsapicasestudy.presentation.adapters.product.ProductAdapter
import com.eugurguner.productsapicasestudy.presentation.viewModels.FavoritesFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FavoritesFragment : Fragment() {
    private lateinit var binding: FragmentFavoritesBinding
    private var adapter: ProductAdapter? = null

    private val viewModel: FavoritesFragmentViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentFavoritesBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = binding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpAdapter()
    }

    private fun setUpAdapter() {
        adapter =
            ProductAdapter(
                productList = mutableListOf(),
                onProductClicked = { product ->
                    navigateToProductDetail(product = product)
                },
                onSaveClicked = { product ->
                    onProductFavorite(product = product)
                },
                onAddToCartClicked = {}
            )

        binding.recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.recyclerView.adapter = adapter

        viewModelListener()
    }

    private fun onProductFavorite(product: Product) {
        var index = -1
        adapter?.productList?.forEachIndexed { i, p ->
            if (p.id == product.id) {
                p.isSaved = !p.isSaved
                index = i
                viewModel.saveOrRemoveProduct(product = p)
            }
        }
        if (index == -1) return
        adapter?.notifyItemChanged(index)
    }

    private fun viewModelListener() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collect { uiState ->
                handleUiState(uiState)
            }
        }
        viewModel.getFavoriteProducts()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun handleUiState(uiState: UIState<List<Product>>) {
        when (uiState) {
            is UIState.Loading -> {
                // Show loading indicator
            }

            is UIState.Success -> {
                val data = uiState.data
                adapter?.productList = data.toMutableList()
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

    private fun navigateToProductDetail(product: Product) {
        FavoritesFragmentDirections.navigateToProductDetail(productModel = product).apply {
            Navigation.findNavController(binding.root).navigate(this)
        }
    }
}