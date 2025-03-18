package org.walletapp.ui.components

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions
import org.walletapp.data.VerifiableCredentialInvitation
import org.walletapp.exceptions.MismatchedRecipientException
import org.walletapp.exceptions.NoDIDException
import org.walletapp.exceptions.ValueNotFoundException
import org.walletapp.managers.PreferencesManager
import org.walletapp.viewmodels.CredentialViewModel

/**
 * A composable function for the Credential tab.
 *
 * This screen allows the user to view their Verifiable Credential if they possess one,
 * and scan a Verifiable Credential Invitation to initiate a credential transfer.
 *
 * @param viewModel The view model providing the data and logic for the Credential tab.
 */
@Composable
fun CredentialTab(viewModel: CredentialViewModel) {
    val context = LocalContext.current

    // Get vc value from the viewModel
    val vc by viewModel.vc

    // Set up variables for managing the confirmation dialog
    var vcInvitation = remember { mutableStateOf<VerifiableCredentialInvitation?>(null) }
    var showConfirmationDialog = remember { mutableStateOf(false) }

    // Set up functionality for the qr code scanner
    val barcodeLauncher = rememberLauncherForActivityResult(
        contract = ScanContract()
    ) { result ->
        if (result?.contents == null) {
            Toast.makeText(context, "Cancelled", Toast.LENGTH_LONG).show()
        } else {
            try {
                vcInvitation.value = viewModel.extractVcInvitation(result.contents)
            } catch (e: Exception) {
                showErrorDialog(context, e)
                return@rememberLauncherForActivityResult
            }

            // Check if the recipient field matches the user's DID
            val recipient = vcInvitation.value?.recipient
            val did = try {
                PreferencesManager.getValue(PreferencesManager.Keys.DID)
            } catch (e: ValueNotFoundException) {
                showErrorDialog(context, NoDIDException("You have not set up a DID"))
                return@rememberLauncherForActivityResult
            }
            if (recipient != did) {
                showErrorDialog(
                    context,
                    MismatchedRecipientException("The recipient field does not match your DID")
                )
            } else {
                showConfirmationDialog.value = true
            }
        }
    }

    // Display the Verifiable Credential
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            Text(
                text = "Verifiable Credential:\n\n$vc",
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .padding(8.dp)
            )
        }

        Spacer(Modifier.height(16.dp))

        // Launch the QR code scanner to scan a Verifiable Credential Invitation
        Button(onClick = { barcodeLauncher.launch(ScanOptions().setOrientationLocked(false)) }) {
            Text("Scan VC Invitation")
        }

        Spacer(Modifier.height(16.dp))

        // Delete the Verifiable Credential
        Button(onClick = { viewModel.deleteVc() }) {
            Text("Delete VC")
        }

        // Show the confirmation dialog
        if (showConfirmationDialog.value && vcInvitation.value != null) {
            CredentialConfirmationDialog(
                onProceed = {
                    viewModel.initiateCredentialTransfer(vcInvitation.value!!)
                    showConfirmationDialog.value = false
                },
                onDismiss = { showConfirmationDialog.value = false },
                vcInvitation.value!!
            )
        }
    }
}