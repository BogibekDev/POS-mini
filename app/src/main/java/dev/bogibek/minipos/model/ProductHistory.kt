package dev.bogibek.minipos.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "product_history")
data class ProductHistory(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") val id: Long? = null,
    @ColumnInfo(name = "name") val name: String? = null,
    @ColumnInfo(name = "note") val note: String? = null,
    @ColumnInfo(name = "barcode") val barCode: String? = null,
    @ColumnInfo(name = "amount") val amount: Int? = null,
    @ColumnInfo(name = "price") val price: Float? = null,
    @ColumnInfo(name = "total") val total: Float? = null,
) : Serializable
