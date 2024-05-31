package it.unipd.dei.music_application.utils

import android.content.Context
import android.widget.Toast

class DisplayToast {
    companion object {
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
}