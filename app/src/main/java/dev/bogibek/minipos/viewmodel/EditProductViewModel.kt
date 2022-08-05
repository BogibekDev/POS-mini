package dev.bogibek.minipos.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.bogibek.minipos.model.Product
import dev.bogibek.minipos.repository.AddProductRepository
import dev.bogibek.minipos.repository.EditProductRepository
import dev.bogibek.minipos.utils.UiStateObject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditProductViewModel @Inject constructor(private val repository: EditProductRepository) :
    ViewModel(){
    private val _saveProduct = MutableStateFlow<UiStateObject<Boolean>>(UiStateObject.EMPTY)
    val saveProduct = _saveProduct

    private val _product = MutableStateFlow<UiStateObject<Product>>(UiStateObject.EMPTY)
    val product = _product

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
}