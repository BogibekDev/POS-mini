package dev.bogibek.minipos.ui.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.annotation.Nullable
import androidx.core.os.bundleOf
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.zxing.integration.android.IntentIntegrator
import dagger.hilt.android.AndroidEntryPoint
import dev.bogibek.minipos.R
import dev.bogibek.minipos.adapter.ProductAdapter
import dev.bogibek.minipos.databinding.FragmentHomeBinding
import dev.bogibek.minipos.utils.Constants.PRODUCT_ID
import dev.bogibek.minipos.utils.UiStateList
import dev.bogibek.minipos.utils.viewBinding
import dev.bogibek.minipos.viewmodel.HomeViewModel
import kotlinx.coroutines.Job


@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {
    private val binding by viewBinding { FragmentHomeBinding.bind(it) }
    private val viewModel: HomeViewModel by viewModels()
    private val adapter by lazy { ProductAdapter() }
    var searchJob: Job? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getQuery().observe(viewLifecycleOwner) {
            if (it != null)
                filter(it)
            else
                viewModel.getProducts()
        }

        setupUi()
    }

    override fun onResume() {
        super.onResume()
        viewModel.getProducts()
    }


    @SuppressLint("ClickableViewAccessibility")
    private fun setupUi() {
        setupObservers()
        binding.apply {
            rvListProducts.adapter = adapter
            bAdd.setOnClickListener {
                findNavController().navigate(R.id.action_homeFragment_to_addProductsFragment)
            }
            etSearch.addTextChangedListener {
                filter(it.toString())
            }
            etSearch.setOnTouchListener { _, event ->
                val DRAWABLE_LEFT = 0
                val DRAWABLE_TOP = 1
                val DRAWABLE_RIGHT = 2
                val DRAWABLE_BOTTOM = 3
                if (event.action == MotionEvent.ACTION_UP) {
                    if (event.rawX >= etSearch.right - etSearch.compoundDrawables[DRAWABLE_RIGHT].bounds.width()) {
                        scanBarCode()
                        return@setOnTouchListener true
                    }

                }

                false
            }
            ivHistory.setOnClickListener {
                findNavController().navigate(R.id.action_homeFragment_to_historyFragment)
            }
        }
        adapter.onItemClick = {
            findNavController().navigate(
                R.id.action_homeFragment_to_saleProductsFragment,
                bundleOf(PRODUCT_ID to it.id)
            )
        }
        adapter.onItemLongClick = {
            findNavController().navigate(
                R.id.action_homeFragment_to_editProductFragment,
                bundleOf(PRODUCT_ID to it.id)
            )
        }
    }

    private fun scanBarCode() {
        try {
            val intentIntegrator = IntentIntegrator.forSupportFragment(this@HomeFragment)
            intentIntegrator.setPrompt("Scan a barcode or QR Code")
            intentIntegrator.setOrientationLocked(false)
            intentIntegrator.initiateScan()

        } catch (e: Exception) {

        }
    }


    private fun setupObservers() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.products.collect {
                when (it) {
                    is UiStateList.LOADING -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }
                    is UiStateList.SUCCESS -> {
                        binding.progressBar.visibility = View.GONE
                        adapter.mySubmitList(it.data)
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
                binding.etSearch.setText(intentResult.contents)
                searchJob?.cancel()
                searchJob = viewLifecycleOwner.lifecycleScope.launchWhenCreated {
                    viewModel.setQuery(intentResult.contents)
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun filter(s: String) {
        adapter.filter.filter(s)
    }

}