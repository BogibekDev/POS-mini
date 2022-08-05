package dev.bogibek.minipos.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "products")
data class Product(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") val id: Long? = null,
    @ColumnInfo(name = "name") val name: String? = null,
    @ColumnInfo(name = "note") val note: String? = null,
    @ColumnInfo(name = "barcode") val barCode: String? = null,
    @ColumnInfo(name = "amount") var amount: Int? = null,
    @ColumnInfo(name = "price") val price: Float? = null,
    @ColumnInfo(name = "total") var total: Float? = null,
) : Serializable
