package dev.bogibek.minipos.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.bogibek.minipos.model.Product
import dev.bogibek.minipos.repository.ProductRepository
import dev.bogibek.minipos.utils.UiStateList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val repository: ProductRepository) : ViewModel() {

    private val _products = MutableStateFlow<UiStateList<Product>>(UiStateList.EMPTY)
    val products = _products

    private var query = MutableLiveData<String>()

    fun getProducts() = viewModelScope.launch {
        _products.value = UiStateList.LOADING
        try {
            val products = repository.getAllProducts()
            _products.value = UiStateList.SUCCESS(products)
        } catch (e: Exception) {
            _products.value = UiStateList.ERROR(e.localizedMessage ?: "Failed")
        }
    }

    fun setQuery(query: String) {
        this.query.value = query
    }

    fun getQuery(): LiveData<String> {
        return query
    }


}