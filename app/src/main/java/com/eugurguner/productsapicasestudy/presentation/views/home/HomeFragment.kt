package com.eugurguner.productsapicasestudy.presentation.views.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.eugurguner.productsapicasestudy.core.UIState
import com.eugurguner.productsapicasestudy.databinding.FragmentHomeBinding
import com.eugurguner.productsapicasestudy.domain.model.Product
import com.eugurguner.productsapicasestudy.presentation.adapters.product.ProductAdapter
import com.eugurguner.productsapicasestudy.presentation.viewModels.HomeFragmentViewModel
import com.eugurguner.productsapicasestudy.presentation.viewModels.MainActivityViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment :
    Fragment(),
    SwipeRefreshLayout.OnRefreshListener {
    private lateinit var binding: FragmentHomeBinding
    private var adapter: ProductAdapter? = null

    private val viewModel: HomeFragmentViewModel by viewModels()
    private val mainActivityViewModel: MainActivityViewModel by activityViewModels()

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
        setUpAdapter()
        setListeners()
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
                onAddToCartClicked = { product ->
                    onAddProductToCart(product = product)
                }
            )

        binding.recyclerView.layoutManager = GridLayoutManager(context, 2, LinearLayoutManager.VERTICAL, false)
        binding.recyclerView.adapter = adapter
    }

    private fun setListeners() {
        viewModelListener()
        binding.swipeRefresh.setOnRefreshListener(this)
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

    private fun onAddProductToCart(product: Product) {
        viewModel.addProductToCart(product = product)
    }

    private fun viewModelListener() {
        lifecycleScope.launch {
            launch {
                viewModel.favoriteBadgeCount.collect { count ->
                    if (count == null) return@collect
                    mainActivityViewModel.updateFavoriteBadgeCountAfterSaveRemoveOperation(count = count)
                }
            }
            launch {
                viewModel.cartBadgeCount.collect { count ->
                    if (count == null) return@collect
                    mainActivityViewModel.updateCartBadgeCountAfterSaveRemoveOperation(count = count)
                }
            }
            launch {
                viewModel.uiState.collect { uiState ->
                    handleUiState(uiState)
                }
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
        HomeFragmentDirections.navigateToProductDetail(productModel = product).apply {
            Navigation.findNavController(binding.root).navigate(this)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onRefresh() {
        binding.swipeRefresh.isRefreshing = false
        adapter?.productList?.clear()
        adapter?.notifyDataSetChanged()
        viewModel.fetchProducts()
    }
}