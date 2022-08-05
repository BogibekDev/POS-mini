package dev.bogibek.minipos.di

import android.app.Application
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.bogibek.minipos.data.local.ProductsDao
import dev.bogibek.minipos.data.local.ProductsDatabase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun appDatabase(context: Application): ProductsDatabase {
        return ProductsDatabase.getAppDBInstance(context)
    }

    @Provides
    @Singleton
    fun productsDao(appDatabase: ProductsDatabase): ProductsDao {
        return appDatabase.getProductsDao()
    }

    
}