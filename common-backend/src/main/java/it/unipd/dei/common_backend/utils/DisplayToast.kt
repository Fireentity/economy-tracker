package it.unipd.dei.common_backend.utils

import android.content.Context
import android.widget.Toast

object DisplayToast {
    fun displayGeneric(context: Context, message: String) {
        Toast.makeText(
            context,
            message,
            Toast.LENGTH_SHORT
        ).show()
    }
}
