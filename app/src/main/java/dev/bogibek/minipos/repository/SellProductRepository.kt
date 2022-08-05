package dev.bogibek.minipos.repository

import dev.bogibek.minipos.data.local.ProductsDao
import dev.bogibek.minipos.model.Product
import dev.bogibek.minipos.model.ProductHistory
import javax.inject.Inject

class SellProductRepository @Inject constructor(private val productsDao: ProductsDao) {

    suspend fun insertProduct(product: Product) = productsDao.insertProductsToDB(product)
    suspend fun getProductByBarcode(barcode: String) = productsDao.getSoldProductByBarcode(barcode)
    suspend fun getProductById(id: Long) = productsDao.getProductById(id)
    suspend fun deleteProduct(id: Long) = productsDao.deleteProductFromDB(id)
    suspend fun insertProductToHistoryDB(product: ProductHistory) =
        productsDao.insertProductsToHistoryDB(product)
}