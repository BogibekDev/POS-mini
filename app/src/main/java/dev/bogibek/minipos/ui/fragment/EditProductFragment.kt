package dev.bogibek.minipos.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.Nullable
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.zxing.integration.android.IntentIntegrator
import dagger.hilt.android.AndroidEntryPoint
import dev.bogibek.minipos.R
import dev.bogibek.minipos.databinding.FragmentEditProductBinding
import dev.bogibek.minipos.model.Product
import dev.bogibek.minipos.utils.Constants.PRODUCT_ID
import dev.bogibek.minipos.utils.UiStateObject
import dev.bogibek.minipos.utils.toast
import dev.bogibek.minipos.utils.viewBinding
import dev.bogibek.minipos.viewmodel.EditProductViewModel
import dev.bogibek.minipos.viewmodel.HomeViewModel


@AndroidEntryPoint
class EditProductFragment : BaseFragment(R.layout.fragment_edit_product) {

    private val binding by viewBinding { FragmentEditProductBinding.bind(it) }
    private val viewModel: EditProductViewModel by viewModels()
    private var id: Long? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            id = it.getLong(PRODUCT_ID)
            viewModel.getProductById(id!!)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUi()
        observeViewModel()

    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.product.collect {
                when (it) {
                    is UiStateObject.LOADING -> {
                        isLoading(true, binding.loading.loading)
                    }

                    is UiStateObject.SUCCESS -> {
                        isLoading(false, binding.loading.loading)
                        id = it.data.id
                        val product = it.data
                        binding.apply {
                            etProductName.setText(product.name)
                            etBarCode.setText(product.barCode)
                            etPrice.setText(product.price.toString())
                            etAmount.setText(product.amount.toString())
                            etTotal.setText(product.total.toString())
                            etDescription.setText(product.note)
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

        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.saveProduct.collect {
                when (it) {
                    is UiStateObject.LOADING -> {
                        isLoading(true, binding.loading.loading)
                    }

                    is UiStateObject.SUCCESS -> {
                        isLoading(false, binding.loading.loading)
                        findNavController().navigate(R.id.action_editProductFragment_to_homeFragment)

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

    private fun setupUi() {
        binding.apply {

            scanBarCode.setOnClickListener {
                try {
                    val intentIntegrator = IntentIntegrator.forSupportFragment(this@EditProductFragment)
                    intentIntegrator.setPrompt("Scan a barcode or QR Code")
                    intentIntegrator.setOrientationLocked(false)
                    intentIntegrator.initiateScan()
                }catch(e: Exception){

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

                    val product = Product(
                        id = id,
                        name = productName,
                        note = productDescription,
                        barCode = productBarCode,
                        price = productPrice,
                        amount = productAmount,
                        total = productTotal)

                    viewModel.saveProducts(product)

                } else {
                    toast("To'liq to'ldirng")
                }

            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, @Nullable data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (intentResult != null) {
            if (intentResult.contents == null) {
                Toast.makeText(requireContext(), "Cancelled", Toast.LENGTH_SHORT).show()
            } else {
                binding.etBarCode.setText(intentResult.contents)
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }



}