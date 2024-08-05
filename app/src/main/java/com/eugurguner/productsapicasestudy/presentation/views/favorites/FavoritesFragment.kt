package com.eugurguner.productsapicasestudy.presentation.views.favorites

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.eugurguner.productsapicasestudy.core.AppEvents
import com.eugurguner.productsapicasestudy.core.UIState
import com.eugurguner.productsapicasestudy.databinding.FragmentFavoritesBinding
import com.eugurguner.productsapicasestudy.domain.model.Product
import com.eugurguner.productsapicasestudy.presentation.adapters.product.ProductAdapter
import com.eugurguner.productsapicasestudy.presentation.viewModels.FavoritesFragmentViewModel
import com.eugurguner.productsapicasestudy.presentation.viewModels.MainActivityViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FavoritesFragment : Fragment() {
    private lateinit var binding: FragmentFavoritesBinding
    private var adapter: ProductAdapter? = null

    private val viewModel: FavoritesFragmentViewModel by viewModels()

    private val mainActivityViewModel: MainActivityViewModel by activityViewModels()

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
                onAddToCartClicked = {
                    viewModel.addProductToCart(product = it)
                }
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
        lifecycleScope.launch {
            launch {
                viewModel.uiState.collect { uiState ->
                    handleUiState(uiState)
                }
            }
            launch {
                viewModel.appEvents.collect { appEvent ->
                    when (appEvent) {
                        AppEvents.None -> {}
                        AppEvents.OnCartBadgeUpdate -> {
                            mainActivityViewModel.updateCartBadgeCount()
                            viewModel.onEventHandled()
                        }

                        AppEvents.OnFavoriteBadgeUpdate -> {
                            mainActivityViewModel.updateFavoriteBadgeCount()
                            viewModel.onEventHandled()
                        }
                    }
                }
            }
        }
        viewModel.getFavoriteProducts()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun handleUiState(uiState: UIState<List<Product>>) {
        when (uiState) {
            is UIState.Loading -> {
                onLoadingStateChanged(isLoading = true)
            }

            is UIState.Success -> {
                val data = uiState.data
                adapter?.productList = data.toMutableList()
                adapter?.notifyDataSetChanged()
                onLoadingStateChanged(isLoading = false)
            }

            is UIState.Error -> {
                loadEmptyView()
                Toast.makeText(context, uiState.message, Toast.LENGTH_SHORT).show()
            }

            is UIState.Empty -> {
                loadEmptyView()
            }
        }
    }

    private fun onLoadingStateChanged(isLoading: Boolean) {
        binding.emptyView.visibility = View.GONE
        if (isLoading) {
            binding.loadingView.visibility = View.VISIBLE
            binding.recyclerView.visibility = View.GONE
        } else {
            binding.loadingView.visibility = View.GONE
            binding.recyclerView.visibility = View.VISIBLE
        }
    }

    private fun loadEmptyView() {
        binding.loadingView.visibility = View.GONE
        binding.recyclerView.visibility = View.GONE
        binding.emptyView.visibility = View.VISIBLE
    }

    private fun navigateToProductDetail(product: Product) {
        FavoritesFragmentDirections.navigateToProductDetail(productModel = product).apply {
            Navigation.findNavController(binding.root).navigate(this)
        }
    }
}