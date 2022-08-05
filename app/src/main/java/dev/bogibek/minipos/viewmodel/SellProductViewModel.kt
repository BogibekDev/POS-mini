package dev.bogibek.minipos.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.bogibek.minipos.model.Product
import dev.bogibek.minipos.model.ProductHistory
import dev.bogibek.minipos.repository.SellProductRepository
import dev.bogibek.minipos.utils.UiStateObject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SellProductViewModel @Inject constructor(private val repository: SellProductRepository) :
    ViewModel() {
    private val _saveProduct = MutableStateFlow<UiStateObject<Boolean>>(UiStateObject.EMPTY)
    val saveProduct = _saveProduct

    private val _product = MutableStateFlow<UiStateObject<Product>>(UiStateObject.EMPTY)
    val product = _product

    private val _soldProduct = MutableStateFlow<UiStateObject<ProductHistory>>(UiStateObject.EMPTY)
    val soldProduct = _soldProduct

    private val _deleteProduct = MutableStateFlow<UiStateObject<Boolean>>(UiStateObject.EMPTY)
    val deleteProduct = _deleteProduct

    private val _saveProductToHistory =
        MutableStateFlow<UiStateObject<Boolean>>(UiStateObject.EMPTY)
    val saveProductToHistory = _saveProductToHistory

    fun saveProducts(product: Product) = viewModelScope.launch {
        _saveProduct.value = UiStateObject.LOADING

        try {
            repository.insertProduct(product)
            _saveProduct.value = UiStateObject.SUCCESS(true)

        } catch (e: Exception) {
            _saveProduct.value = UiStateObject.ERROR(e.localizedMessage ?: "Failed")
        }
    }

    fun getProductById(id: Long) = viewModelScope.launch {
        _product.value = UiStateObject.LOADING

        try {
            val product = repository.getProductById(id)
            _product.value = UiStateObject.SUCCESS(product)

        } catch (e: Exception) {
            _product.value = UiStateObject.ERROR(e.localizedMessage ?: "Failed")
        }


    }

    fun deleteProduct(id: Long) = viewModelScope.launch {
        _deleteProduct.value = UiStateObject.LOADING

        try {
            repository.deleteProduct(id)
            _deleteProduct.value = UiStateObject.SUCCESS(true)

        } catch (e: Exception) {
            _deleteProduct.value = UiStateObject.ERROR(e.localizedMessage ?: "Failed")
        }
    }

    fun saveProductToHistory(product: ProductHistory) = viewModelScope.launch {
        _saveProductToHistory.value = UiStateObject.LOADING

        try {
            repository.insertProductToHistoryDB(product)
            _saveProductToHistory.value = UiStateObject.SUCCESS(true)

        } catch (e: Exception) {
            _saveProductToHistory.value = UiStateObject.ERROR(e.localizedMessage ?: "Failed")
        }
    }

    fun getSoldProductByBarcode(barcode: String) = viewModelScope.launch {
        _soldProduct.value = UiStateObject.LOADING
        try {
            val response = repository.getProductByBarcode(barcode)
            if (response != null)
                _soldProduct.value = UiStateObject.SUCCESS(response)
            else
                _soldProduct.value = UiStateObject.SUCCESS(ProductHistory())

        } catch (e: Exception) {
            _soldProduct.value = UiStateObject.ERROR(e.localizedMessage ?: "Failed")
        }
    }
}