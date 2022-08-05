package dev.bogibek.minipos.ui.fragment

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import dev.bogibek.minipos.R
import dev.bogibek.minipos.adapter.ProductAdapter
import dev.bogibek.minipos.databinding.FragmentHistoryBinding
import dev.bogibek.minipos.model.Product
import dev.bogibek.minipos.utils.UiStateList
import dev.bogibek.minipos.utils.toProduct
import dev.bogibek.minipos.utils.viewBinding
import dev.bogibek.minipos.viewmodel.HomeViewModel
import dev.bogibek.minipos.viewmodel.SoldProductsViewModel

@AndroidEntryPoint
class HistoryFragment : Fragment(R.layout.fragment_history) {
    private val binding by viewBinding { FragmentHistoryBinding.bind(it) }
    private val viewModel: SoldProductsViewModel by viewModels()
    private val adapter by lazy { ProductAdapter() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUi()
    }

    private fun setupUi() {
        viewModel.getSoldProducts()
        setupObservers()
        binding.apply {
            rvListProducts.adapter=adapter
            btnBack.setOnClickListener {
                requireActivity().onBackPressed()
            }
            etSearch.addTextChangedListener {
                adapter.filter.filter(it)
            }
        }
    }

    private fun setupObservers() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.soldProducts.collect {
                when (it) {
                    is UiStateList.LOADING -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }
                    is UiStateList.SUCCESS -> {
                        binding.progressBar.visibility = View.GONE
                        val products = ArrayList<Product>()
                        it.data.forEach { productsHist ->
                            products.add(productsHist.toProduct())
                        }
                        adapter.mySubmitList(products.toList())
                    }
                    is UiStateList.ERROR -> {
                        binding.progressBar.visibility = View.GONE
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                    }
                    else -> Unit
                }
            }
        }
    }

}