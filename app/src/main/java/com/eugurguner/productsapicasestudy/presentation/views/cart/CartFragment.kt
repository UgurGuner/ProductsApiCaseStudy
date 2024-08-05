package com.eugurguner.productsapicasestudy.presentation.views.cart

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.eugurguner.productsapicasestudy.core.StaticVariables
import com.eugurguner.productsapicasestudy.core.UIState
import com.eugurguner.productsapicasestudy.core.extensions.formatPrice
import com.eugurguner.productsapicasestudy.databinding.FragmentCartBinding
import com.eugurguner.productsapicasestudy.domain.model.Product
import com.eugurguner.productsapicasestudy.presentation.adapters.cart.CartAdapter
import com.eugurguner.productsapicasestudy.presentation.viewModels.CartFragmentViewModel
import com.eugurguner.productsapicasestudy.presentation.viewModels.MainActivityViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CartFragment : Fragment() {
    private lateinit var binding: FragmentCartBinding
    private var adapter: CartAdapter? = null

    private val viewModel: CartFragmentViewModel by viewModels()
    private val mainActivityViewModel: MainActivityViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentCartBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = binding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpAdapter()
        viewModelListener()
        setOnClickListener()
    }

    private fun setUpAdapter() {
        adapter =
            CartAdapter(
                cartProductList = mutableListOf(),
                onDecrease = {
                    viewModel.decreaseProductQuantity(it)
                },
                onIncrease = {
                    viewModel.increaseProductQuantity(it)
                }
            )

        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    }

    private fun viewModelListener() {
        lifecycleScope.launch {
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
        viewModel.getCartProducts()
    }

    private fun setOnClickListener() {
        binding.btnComplete.setOnClickListener {}
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun handleUiState(uiState: UIState<List<Product>>) {
        when (uiState) {
            is UIState.Loading -> {
                // Show loading indicator
            }

            is UIState.Success -> {
                val data = uiState.data
                adapter?.cartProductList = data.toMutableList()
                adapter?.notifyDataSetChanged()
                updateUI(list = data)
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

    @SuppressLint("SetTextI18n")
    private fun updateUI(list: List<Product>) {
        val totalPrice = list.sumOf { (it.price * it.quantitiy).toInt() }
        binding.txtProductsTotalPrice.text = "${totalPrice.formatPrice()}${StaticVariables.CURRENCY_SYMBOL}"
    }
}