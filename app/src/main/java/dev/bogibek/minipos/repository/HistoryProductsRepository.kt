package dev.bogibek.minipos.repository

import dev.bogibek.minipos.data.local.ProductsDao
import dev.bogibek.minipos.model.Product
import dev.bogibek.minipos.model.ProductHistory
import javax.inject.Inject

class HistoryProductsRepository @Inject constructor(private val productsDao: ProductsDao) {

    suspend fun getAllProductsFromHistory()=
        productsDao.getProductsFromHistoryDB()
}