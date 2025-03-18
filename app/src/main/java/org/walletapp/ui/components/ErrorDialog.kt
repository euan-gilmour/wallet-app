package org.walletapp.ui.components

import android.app.AlertDialog
import android.content.Context

/**
 * A simple function for displaying an error dialog
 * for a given exception
 *
 * @param context the context of the application
 * @param exception the exception that caused the error
 */
fun showErrorDialog(context: Context, exception: Exception) {
    AlertDialog.Builder(context).apply {
        setTitle("A ${exception::class.simpleName} has occurred:")
        setMessage(exception.message)
        setPositiveButton("OK") { dialog, _ ->
            dialog.dismiss()
        }
        show()
    }
}

