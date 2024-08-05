package com.eugurguner.productsapicasestudy.presentation.views.detail

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.eugurguner.productsapicasestudy.R
import com.eugurguner.productsapicasestudy.core.StaticVariables
import com.eugurguner.productsapicasestudy.core.extensions.formatPrice
import com.eugurguner.productsapicasestudy.databinding.FragmentProductDetailBinding
import com.eugurguner.productsapicasestudy.presentation.viewModels.MainActivityViewModel
import com.eugurguner.productsapicasestudy.presentation.viewModels.ProductDetailFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProductDetailFragment : Fragment() {
    private lateinit var binding: FragmentProductDetailBinding
    private val args: ProductDetailFragmentArgs by navArgs()

    private val viewModel: ProductDetailFragmentViewModel by viewModels()
    private val mainActivityViewModel: MainActivityViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentProductDetailBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = binding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListeners()
        updateUI()
    }

    @SuppressLint("SetTextI18n")
    private fun updateUI() {
        binding.toolBarView.setTitle(args.productModel.model)
        binding.txtProductModel.text = args.productModel.model
        binding.txtProductPrice.text = "${args.productModel.price.formatPrice()}${StaticVariables.CURRENCY_SYMBOL}"
        binding.txtProductDescription.text = args.productModel.description
        val imageUrl = args.productModel.imageUrl
        if (imageUrl.isNotBlank()) {
            Glide.with(binding.root).load(imageUrl).into(binding.imgProduct)
        }
        updateStar()
    }

    private fun updateStar() {
        if (args.productModel.isSaved) {
            binding.imgSave.setImageResource(R.drawable.saved_icon)
        } else {
            binding.imgSave.setImageResource(R.drawable.unsaved_icon)
        }
    }

    private fun setListeners() {
        binding.toolBarView.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
        binding.btnSave.setOnClickListener {
            args.productModel.isSaved = !args.productModel.isSaved
            viewModel.saveOrRemoveProduct(product = args.productModel)
            updateStar()
        }
        binding.btnAddToCart.setOnClickListener {}

        lifecycleScope.launch {
            viewModel.favoriteBadgeCount.collect { count ->
                if (count == null) return@collect
                mainActivityViewModel.updateFavoriteBadgeCountAfterSaveRemoveOperation(count = count)
            }
        }
    }
}