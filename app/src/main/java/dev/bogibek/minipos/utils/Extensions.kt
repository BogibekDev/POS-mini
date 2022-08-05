package dev.bogibek.minipos.utils

import android.widget.Toast
import androidx.fragment.app.Fragment
import dev.bogibek.minipos.model.Product
import dev.bogibek.minipos.model.ProductHistory


fun Fragment.toast(message: String) {
    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
}
fun Product.toHistory()=ProductHistory(
    id, name, note, barCode, amount, price, total
)

fun ProductHistory.toProduct()=Product(
    id, name, note, barCode, amount, price, total
)