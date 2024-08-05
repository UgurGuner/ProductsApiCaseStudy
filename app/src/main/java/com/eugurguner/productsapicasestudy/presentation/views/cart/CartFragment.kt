package com.eugurguner.productsapicasestudy.presentation.views.cart

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
        mainActivityViewModel.updateCartBadgeCount()
        when (uiState) {
            is UIState.Loading -> {}

            is UIState.Success -> {
                val data = uiState.data
                adapter?.cartProductList = data.toMutableList()
                adapter?.notifyDataSetChanged()
                updateUI(list = data)
            }

            is UIState.Error -> {
                Toast.makeText(context, uiState.message, Toast.LENGTH_SHORT).show()
                updateUI(emptyList())
            }

            is UIState.Empty -> {
                adapter?.cartProductList?.clear()
                adapter?.notifyDataSetChanged()
                updateUI(emptyList())
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun updateUI(list: List<Product>) {
        val totalPrice = list.sumOf { (it.price * it.quantitiy).toInt() }
        if (totalPrice != 0) {
            binding.txtProductsTotalPrice.text = "${totalPrice.formatPrice()}${StaticVariables.CURRENCY_SYMBOL}"
        } else {
            binding.txtProductsTotalPrice.text = ""
        }
        if (list.isEmpty()) {
            binding.recyclerView.visibility = View.GONE
            binding.emptyView.visibility = View.VISIBLE
        } else {
            binding.recyclerView.visibility = View.VISIBLE
            binding.emptyView.visibility = View.GONE
        }
    }
}