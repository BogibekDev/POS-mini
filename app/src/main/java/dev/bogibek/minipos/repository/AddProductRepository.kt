package dev.bogibek.minipos.repository

import dev.bogibek.minipos.data.local.ProductsDao
import dev.bogibek.minipos.model.Product
import javax.inject.Inject

class AddProductRepository@Inject constructor(private val productsDao: ProductsDao) {

    suspend fun insertProduct(product: Product) = productsDao.insertProductsToDB(product)
    suspend fun getProductByBarcode(barcode: String)=productsDao.getProductByBarcode(barcode)
}