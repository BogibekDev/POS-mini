package dev.bogibek.minipos.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import dev.bogibek.minipos.model.Product
import dev.bogibek.minipos.model.ProductHistory

@Dao
interface ProductsDao {

    @Query("SELECT * FROM products")
    suspend fun getProductsFromDB(): List<Product>

    @Query("SELECT * FROM products WHERE id= :id")
    suspend fun getProductById(id: Long): Product

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProductsToDB(product: Product)


    @Query("DELETE FROM products WHERE id= :id")
    suspend fun deleteProductFromDB(id: Long)

    @Query("SELECT * FROM products WHERE barcode= :barcode")
    suspend fun getProductByBarcode(barcode: String): Product?

    //history
    @Query("SELECT * FROM product_history")
    suspend fun getProductsFromHistoryDB(): List<ProductHistory>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProductsToHistoryDB(product: ProductHistory)


    @Query("DELETE FROM product_history WHERE id= :id")
    suspend fun deleteProductFromHistoryDB(id: Long)


    @Query("SELECT * FROM product_history WHERE barcode= :barcode")
    suspend fun getSoldProductByBarcode(barcode: String): ProductHistory?
}