package dev.bogibek.minipos.repository

import dev.bogibek.minipos.data.local.ProductsDao
import javax.inject.Inject

class ProductRepository @Inject constructor(private val productsDao: ProductsDao) {

    suspend fun getAllProducts() = productsDao.getProductsFromDB()
}