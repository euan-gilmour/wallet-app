package org.walletapp.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import org.walletapp.data.VerifiableCredentialInvitation

/**
 * A confirmation dialog shown before creating and sending a Verifiable Credential
 *
 * @param onProceed The function to be called when the "Proceed" button is pressed
 * @param onDismiss The function to be called when the "Cancel" button is pressed
 * @param vcInvitation The Verifiable Credential invitation
 */
@Composable
fun CredentialConfirmationDialog(
    onProceed: () -> Unit,
    onDismiss: () -> Unit,
    vcInvitation: VerifiableCredentialInvitation
) {
    AlertDialog(
        title = { Text("VC Confirmation") },
        text = {
            Column {
                Text(
                    """
                    You have been offered a Verifiable Credential.
                    
                    Issuer: ${vcInvitation.issuer}
                    Recipient: ${vcInvitation.recipient}
                    Type: ${vcInvitation.type}
                    Web Socket URL: ${vcInvitation.webSocketsUrl}
                    
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