package org.walletapp.ui.components

import androidx.compose.material3.AlertDialog
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.material3.Button
import org.walletapp.data.VerifiablePresentationRequest

/**
 * A confirmation dialog shown before creating and sending a Verifiable Presentation
 *
 * @param onProceed The function to be called when the "Proceed" button is pressed
 * @param onDismiss The function to be called when the "Cancel" button is pressed
 * @param vpRequest The Verifiable Presentation request
 */
@Composable
fun PresentationConfirmationDialog(
    onProceed: () -> Unit,
    onDismiss: () -> Unit,
    vpRequest: VerifiablePresentationRequest
) {
    AlertDialog(
        title = { Text("VP Confirmation") },
        text = {
            Column {
                Text(
                    """
                    You are about to create and send a Verifiable Presentation.
                    
                    Domain: ${vpRequest.domain}
                    App Name: ${vpRequest.appName}
                    Web Socket URL: ${vpRequest.webSocketsUrl}
                    
                    Do you wish to proceed?
                """.trimIndent()
                )
            }
        },
        confirmButton = {
            Button(onClick = onProceed) {
                Text("Proceed")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancel")
            }
        },
        onDismissRequest = onDismiss
    )
}