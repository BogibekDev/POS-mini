package dev.bogibek.minipos.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import dev.bogibek.minipos.R
import dev.bogibek.minipos.databinding.FragmentSaleProductsBinding
import dev.bogibek.minipos.model.Product
import dev.bogibek.minipos.model.ProductHistory
import dev.bogibek.minipos.utils.*
import dev.bogibek.minipos.viewmodel.SellProductViewModel

@AndroidEntryPoint
class SaleProductsFragment : BaseFragment(R.layout.fragment_sale_products) {
    private val binding by viewBinding { FragmentSaleProductsBinding.bind(it) }
    private val viewModel: SellProductViewModel by viewModels()
    private lateinit var product: Product
    private var amount: Int = 0
    private var id: Long? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            id = it.getLong(Constants.PRODUCT_ID)
            viewModel.getProductById(id!!)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUi()
        observeViewModel()
    }

    private fun setupUi() {
        binding.apply {
            btnSale.setOnClickListener {

                if (etAmount.text!!.isNotEmpty()) {
                    amount = etAmount.text.toString().toInt()

                    if (amount < product.amount!!) {
                        product.amount = product.amount!! - amount
                        product.total = product.amount!! * product.price!!
                        viewModel.saveProducts(product)
                    } else
                        if (amount == product.amount) {
                            viewModel.deleteProduct(id!!)
                        } else
                            if (amount > product.amount!!) {
                                toast("Omborda buncha mahsulot yo'q")
                            }

                } else
                    toast("Maydonni to'ldiring")
            }
            btnBack.setOnClickListener {
                requireActivity().onBackPressed()
            }
        }
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.product.collect {
                when (it) {
                    is UiStateObject.LOADING -> {
                        isLoading(true, binding.loading.loading)
                    }

                    is UiStateObject.SUCCESS -> {
                        isLoading(false, binding.loading.loading)
                        id = it.data.id
                        product = it.data
                        binding.apply {
                            tvName.text = product.name
                            tvBarcode.text = product.barCode
                            tvPrice.text = product.price.toString()
                            tvAmount.text = product.amount.toString()
                            tvDescription.text = product.note
                        }
                    }

                    is UiStateObject.ERROR -> {
                        isLoading(false, binding.loading.loading)
                        toast(it.message)

                    }
                    else -> Unit
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.saveProduct.collect {
                when (it) {
                    is UiStateObject.LOADING -> {
                        isLoading(true, binding.loading.loading)
                    }

                    is UiStateObject.SUCCESS -> {
                        isLoading(false, binding.loading.loading)
                        viewModel.getSoldProductByBarcode(product.barCode!!)
                    }

                    is UiStateObject.ERROR -> {
                        isLoading(false, binding.loading.loading)
                        toast(it.message)

                    }
                    else -> Unit
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.deleteProduct.collect {
                when (it) {
                    is UiStateObject.LOADING -> {
                        isLoading(true, binding.loading.loading)
                    }

                    is UiStateObject.SUCCESS -> {
                        isLoading(false, binding.loading.loading)
                        viewModel.getSoldProductByBarcode(product.barCode!!)
                    }

                    is UiStateObject.ERROR -> {
                        isLoading(false, binding.loading.loading)
                        toast(it.message)

                    }
                    else -> Unit
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.soldProduct.collect {
                when (it) {
                    is UiStateObject.LOADING -> {
                        isLoading(true, binding.loading.loading)
                    }

                    is UiStateObject.SUCCESS -> {
                        isLoading(false, binding.loading.loading)
                        saveToHistory(it.data)
                    }

                    is UiStateObject.ERROR -> {
                        isLoading(false, binding.loading.loading)
                        toast(it.message)

                    }
                    else -> Unit
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.saveProductToHistory.collect {
                when (it) {
                    is UiStateObject.LOADING -> {
                        isLoading(true, binding.loading.loading)
                    }

                    is UiStateObject.SUCCESS -> {
                        isLoading(false, binding.loading.loading)
                        findNavController().navigate(R.id.action_saleProductsFragment_to_homeFragment)
                    }

                    is UiStateObject.ERROR -> {
                        isLoading(false, binding.loading.loading)
                        toast(it.message)

                    }
                    else -> Unit
                }
            }
        }
    }

    private fun saveToHistory(data: ProductHistory) {
        if (data.amount != null) {
            product.amount = amount + data.amount
            product.total = product.amount!! * product.price!!
            viewModel.saveProductToHistory(product.toHistory())
        } else {
            product.amount = amount
            product.total = product.amount!! * product.price!!
            viewModel.saveProductToHistory(product.toHistory())
        }
    }
}