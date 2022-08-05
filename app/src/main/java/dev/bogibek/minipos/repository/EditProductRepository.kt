package dev.bogibek.minipos.repository

import dev.bogibek.minipos.data.local.ProductsDao
import dev.bogibek.minipos.model.Product
import javax.inject.Inject

class EditProductRepository@Inject constructor(private val productsDao: ProductsDao) {

    suspend fun insertProduct(product: Product) = productsDao.insertProductsToDB(product)
    suspend fun getProductById(id:Long)=productsDao.getProductById(id)
}