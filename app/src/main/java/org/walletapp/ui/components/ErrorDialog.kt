package org.walletapp.ui.components

import android.app.AlertDialog
import android.content.Context

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

