package dev.bogibek.minipos.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.bogibek.minipos.model.Product
import dev.bogibek.minipos.repository.AddProductRepository
import dev.bogibek.minipos.utils.UiStateObject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddProductViewModel @Inject constructor(private val repository: AddProductRepository) :
    ViewModel() {

    private val _saveProduct = MutableStateFlow<UiStateObject<Boolean>>(UiStateObject.EMPTY)
    val saveProduct = _saveProduct

    private val _savedProduct = MutableStateFlow<UiStateObject<Product>>(UiStateObject.EMPTY)
    val savedProduct = _savedProduct

    fun saveProducts(product: Product) = viewModelScope.launch {
        _saveProduct.value = UiStateObject.LOADING

        try {
            repository.insertProduct(product)
            _saveProduct.value = UiStateObject.SUCCESS(true)

        } catch (e: Exception) {
            _saveProduct.value = UiStateObject.ERROR(e.localizedMessage ?: "Failed")
        }
    }

    fun getSavedProductByBarcode(barcode: String) = viewModelScope.launch {
        _savedProduct.value = UiStateObject.LOADING

        try {
            val product = repository.getProductByBarcode(barcode)
            if (product != null) {
                _savedProduct.value = UiStateObject.SUCCESS(product)
            } else {
                _savedProduct.value = UiStateObject.SUCCESS(Product())
            }


        } catch (e: Exception) {
            _savedProduct.value = UiStateObject.ERROR(e.localizedMessage ?: "Failed")
        }
    }
}