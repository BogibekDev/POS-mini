package dev.bogibek.minipos.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.bogibek.minipos.model.ProductHistory
import dev.bogibek.minipos.repository.HistoryProductsRepository
import dev.bogibek.minipos.utils.UiStateList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SoldProductsViewModel @Inject constructor(private val repository: HistoryProductsRepository) :
    ViewModel() {


    private val _soldProducts = MutableStateFlow<UiStateList<ProductHistory>>(UiStateList.EMPTY)
    val soldProducts = _soldProducts

    fun getSoldProducts() = viewModelScope.launch {
        _soldProducts.value = UiStateList.LOADING
        try {
            val products = repository.getAllProductsFromHistory()
            _soldProducts.value = UiStateList.SUCCESS(products)
        } catch (e: Exception) {
            _soldProducts.value = UiStateList.ERROR(e.localizedMessage ?: "Failed")
        }
    }
}