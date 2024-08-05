package com.eugurguner.productsapicasestudy.presentation.views.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.eugurguner.productsapicasestudy.R
import com.eugurguner.productsapicasestudy.core.AppEvents
import com.eugurguner.productsapicasestudy.core.UIState
import com.eugurguner.productsapicasestudy.core.sortAndFilter.FilterOptions
import com.eugurguner.productsapicasestudy.core.sortAndFilter.filterList
import com.eugurguner.productsapicasestudy.databinding.FragmentHomeBinding
import com.eugurguner.productsapicasestudy.domain.model.Product
import com.eugurguner.productsapicasestudy.presentation.adapters.product.ProductAdapter
import com.eugurguner.productsapicasestudy.presentation.viewModels.HomeFragmentViewModel
import com.eugurguner.productsapicasestudy.presentation.viewModels.MainActivityViewModel
import com.eugurguner.productsapicasestudy.presentation.viewModels.SharedViewModel
import com.eugurguner.productsapicasestudy.presentation.views.filter.FilterBottomSheetDialogFragment
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
    private val sharedViewModel: SharedViewModel by activityViewModels()

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

        val layoutManager = GridLayoutManager(context, 2, LinearLayoutManager.VERTICAL, false)

        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.adapter = adapter

        // Same result could be achieve by using paging3 library but whole
        // state management structure along with dataSource and adapter structures must have been changed...
        binding.recyclerView.addOnScrollListener(
            object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)

                    if (!viewModel.hasMore) return

                    val lastPosition = layoutManager.findLastVisibleItemPosition()
                    val listSize = adapter?.productList?.count() ?: 0

                    if (!viewModel.isLoading && lastPosition >= listSize - 4) {
                        binding.paginationView.visibility = View.VISIBLE
                        viewModel.fetchProducts()
                    }
                }
            }
        )
    }

    private fun setListeners() {
        viewModelListener()

        binding.swipeRefresh.setOnRefreshListener(this)

        binding.btnFilter.setOnClickListener {
            val filterBottomSheet = FilterBottomSheetDialogFragment()
            filterBottomSheet.show(parentFragmentManager, filterBottomSheet.tag)
        }

        binding.editTextSearch.addTextChangedListener(
            object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

                override fun afterTextChanged(s: Editable?) {
                    val query = s.toString().trim()
                    filterSearchProducts(query)
                }
            }
        )
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun filterSearchProducts(query: String) {
        if (query.isBlank()) {
            loadOriginalProductList()
        } else {
            val products = viewModel.getFetchedProducts()
            val filteredProducts = products.filter { it.name.contains(query) }
            adapter?.productList = filteredProducts.toMutableList()
            adapter?.notifyDataSetChanged()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun loadOriginalProductList() {
        adapter?.productList = viewModel.getFetchedProducts().toMutableList()
        adapter?.notifyDataSetChanged()
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

    @SuppressLint("NotifyDataSetChanged")
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
            launch {
                sharedViewModel.sortOption.observe(viewLifecycleOwner) { sortOption ->
                    viewModel.updateSortOption(sortOption = sortOption)
                }
                sharedViewModel.filterSettings.observe(viewLifecycleOwner) { filterOptions ->
                    checkIfFiltersApplied(filterOptions = filterOptions)
                }
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun handleUiState(uiState: UIState<List<Product>>) {
        when (uiState) {
            is UIState.Loading -> {
                onLoadingStateChanged(isLoading = true)
            }

            is UIState.Success -> {
                val data = uiState.data
                adapter?.productList?.addAll(data)
                adapter?.notifyDataSetChanged()
                sharedViewModel.setFilterOptions(data)
                onLoadingStateChanged(isLoading = false)
            }

            is UIState.Error -> {
                Toast.makeText(context, uiState.message, Toast.LENGTH_SHORT).show()
            }

            is UIState.Empty -> {}
        }
    }

    @SuppressLint("SetTextI18n", "NotifyDataSetChanged")
    private fun checkIfFiltersApplied(filterOptions: FilterOptions) {
        val isFiltersApplied = filterOptions.models.any { it.isSelected } || filterOptions.brands.any { it.isSelected }
        if (isFiltersApplied) {
            binding.txtFiltersInfo.text = "${getString(R.string.filters)} Applied"
            val filteredList = filterList(filterOptions = filterOptions, productList = adapter?.productList ?: mutableListOf())
            if (filteredList.isEmpty()) return
            adapter?.productList = filteredList.toMutableList()
            adapter?.notifyDataSetChanged()
        } else {
            binding.txtFiltersInfo.text = getString(R.string.filters)
            loadOriginalProductList()
        }
    }

    private fun onLoadingStateChanged(isLoading: Boolean) {
        if (isLoading) {
            binding.loadingView.visibility = View.VISIBLE
            binding.loadedView.visibility = View.GONE
        } else {
            binding.paginationView.visibility = View.GONE
            binding.loadingView.visibility = View.GONE
            binding.loadedView.visibility = View.VISIBLE
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
        viewModel.refreshProducts()
    }
}