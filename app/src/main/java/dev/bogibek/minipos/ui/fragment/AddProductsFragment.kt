package dev.bogibek.minipos.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.Nullable
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.zxing.integration.android.IntentIntegrator
import dagger.hilt.android.AndroidEntryPoint
import dev.bogibek.minipos.R
import dev.bogibek.minipos.databinding.FragmentAddProductsBinding
import dev.bogibek.minipos.model.Product
import dev.bogibek.minipos.utils.UiStateObject
import dev.bogibek.minipos.utils.toast
import dev.bogibek.minipos.utils.viewBinding
import dev.bogibek.minipos.viewmodel.AddProductViewModel

@AndroidEntryPoint
class AddProductsFragment : BaseFragment(R.layout.fragment_add_products) {
    private val binding by viewBinding { FragmentAddProductsBinding.bind(it) }
    private val viewModel: AddProductViewModel by viewModels()
    private lateinit var product: Product

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUi()
        observeViewModel()
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.saveProduct.collect {
                when (it) {
                    is UiStateObject.LOADING -> {
                        isLoading(true, binding.loading.loading)
                    }

                    is UiStateObject.SUCCESS -> {
                        isLoading(false, binding.loading.loading)
                        findNavController().navigate(R.id.action_addProductsFragment_to_homeFragment)

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
            viewModel.savedProduct.collect {
                when (it) {
                    is UiStateObject.LOADING -> {
                        isLoading(true, binding.loading.loading)
                    }
                    is UiStateObject.SUCCESS -> {
                        isLoading(false, binding.loading.loading)
                        editProduct(it.data)
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

    private fun editProduct(data: Product) {
        if (data.amount != null) {
            product.amount = data.amount!! + product.amount!!
            viewModel.saveProducts(product)
        } else {
            viewModel.saveProducts(product)
        }

    }

    private fun setupUi() {

        binding.apply {

            scanBarCode.setOnClickListener {
                try {
                    val intentIntegrator =
                        IntentIntegrator.forSupportFragment(this@AddProductsFragment)
                    intentIntegrator.setPrompt("Scan a barcode or QR Code")
                    intentIntegrator.setOrientationLocked(false)
                    intentIntegrator.initiateScan()

                } catch (e: Exception) {

                }
            }

            btnBack.setOnClickListener {
                requireActivity().onBackPressed()
            }

            btnSave.setOnClickListener {
                if (
                    etProductName.text!!.isNotEmpty()
                    && etBarCode.text!!.isNotEmpty()
                    && etPrice.text!!.isNotEmpty()
                    && etAmount.text!!.isNotEmpty()
                    && etTotal.text!!.isNotEmpty()
                    && etDescription.text!!.isNotEmpty()
                ) {
                    val productName = etProductName.text.toString().trim()
                    val productBarCode = etBarCode.text.toString().trim()
                    val productPrice = etPrice.text.toString().trim().toFloat()
                    val productAmount = etAmount.text.toString().trim().toInt()
                    val productTotal = etTotal.text.toString().trim().toFloat()
                    val productDescription = etDescription.text.toString().trim()

                    product = Product(
                        name = productName,
                        note = productDescription,
                        barCode = productBarCode,
                        price = productPrice,
                        amount = productAmount,
                        total = productTotal
                    )

                    viewModel.getSavedProductByBarcode(productBarCode)

                } else {
                    toast("To'liq to'ldirng")
                }

            }
            etAmount.addTextChangedListener {
                if (etPrice.text!!.isNotEmpty() && it.toString().isNotEmpty()) {
                    etTotal.setText(
                        (it.toString().toInt() * etPrice.text.toString().toFloat()).toString()
                    )
                }
            }
        }

    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, @Nullable data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        Log.d("@@@", "onActivityResult: ${intentResult.contents}")
        if (intentResult != null) {
            if (intentResult.contents == null) {
                Toast.makeText(requireContext(), "Cancelled", Toast.LENGTH_SHORT).show()
            } else {
                Log.d("@@@", "onActivityResult: ${intentResult.contents}")
                binding.etBarCode.setText(intentResult.contents)
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }


}