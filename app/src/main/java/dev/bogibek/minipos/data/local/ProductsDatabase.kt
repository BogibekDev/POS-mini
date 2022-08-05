package dev.bogibek.minipos.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import dev.bogibek.minipos.model.Product
import dev.bogibek.minipos.model.ProductHistory

@Database(entities = [Product::class, ProductHistory::class], version = 1)
abstract class ProductsDatabase : RoomDatabase() {

    abstract fun getProductsDao(): ProductsDao


    companion object {
        private var DB_INSTANCE: ProductsDatabase? = null

        fun getAppDBInstance(context: Context): ProductsDatabase {
            if (DB_INSTANCE == null) {
                DB_INSTANCE = Room.databaseBuilder(
                    context.applicationContext,
                    ProductsDatabase::class.java,
                    "DB_TV_SHOWS"
                )
                    .allowMainThreadQueries()
                    .build()
            }
            return DB_INSTANCE!!
        }
    }
}