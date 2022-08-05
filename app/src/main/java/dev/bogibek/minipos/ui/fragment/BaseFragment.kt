package dev.bogibek.minipos.ui.fragment

import android.view.View
import androidx.fragment.app.Fragment

open class BaseFragment(layoutId: Int): Fragment(layoutId) {

    fun isLoading(isLoading: Boolean, view: View) {
        view.visibility = if (isLoading) {
            View.VISIBLE
        } else {
            View.GONE
        }

    }
}