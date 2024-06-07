package it.unipd.dei.common_backend.utils

import android.content.Context
import android.widget.Toast
import it.unipd.dei.common_backend.utils.Constants

object DisplayToast {
    fun displayGeneric(context: Context, message: String) {
        Toast.makeText(
            context,
            message,
            Toast.LENGTH_SHORT
        ).show()
    }

    fun displaySuccess(context: Context) {
        Toast.makeText(
            context,
            Constants.OPERATION_SUCCESSFUL_MESSAGE,
            Toast.LENGTH_SHORT
        ).show()
    }

    fun displayFailure(context: Context) {
        Toast.makeText(
            context,
            Constants.OPERATION_UNSUCCESSFUL_MESSAGE,
            Toast.LENGTH_SHORT
        ).show()
    }
}
