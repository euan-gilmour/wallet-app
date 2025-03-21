package org.walletapp.ui.components

import android.hardware.biometrics.BiometricManager.Authenticators.BIOMETRIC_STRONG
import android.hardware.biometrics.BiometricPrompt
import android.os.CancellationSignal
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions
import org.walletapp.auth.PresentationBiometricCallback
import org.walletapp.data.VerifiablePresentationRequest
import org.walletapp.viewmodels.PresentationViewModel

/**
 * A composable function for the Presentation tab.
 *
 * This screen allows the user to scan a Verifiable Presentation Request to create and send a VP
 *
 * @param viewModel The view model providing the data and logic for the Presentation tab.
 */
@Composable
fun PresentationTab(viewModel: PresentationViewModel) {
    val context = LocalContext.current

    // Set up variables for managing the confirmation dialog
    var vpRequest = remember { mutableStateOf<VerifiablePresentationRequest?>(null) }
    var showConfirmationDialog = remember { mutableStateOf(false) }

    // Set up functionality for the qr code scanner
    val barcodeLauncher = rememberLauncherForActivityResult(
        contract = ScanContract()
    ) { result ->
        if (result?.contents == null) {
            Toast.makeText(context, "Cancelled", Toast.LENGTH_LONG).show()
        } else {
            try {
                vpRequest.value = viewModel.extractVpRequest(result.contents)
            } catch (e: Exception) {
                showErrorDialog(context, e)
                return@rememberLauncherForActivityResult
            }
            showConfirmationDialog.value = true
        }
    }
    val scanOptions = ScanOptions()
    scanOptions.setPrompt("Scan a Verifiable Credential Invitation QR Code")
    scanOptions.setOrientationLocked(false)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Launch the QR code scanner to scan a Verifiable Presentation Request
        Button(onClick = {
            barcodeLauncher.launch(scanOptions)
        }) {
            Text("Scan VP Request")
        }

        // Display the confirmation dialog
        if (showConfirmationDialog.value && vpRequest.value != null) {
            PresentationConfirmationDialog(
                onProceed = {
                    showConfirmationDialog.value = false

                    // Initiate biometric authentication before sending the VP
                    try {
                        val biometricPrompt = BiometricPrompt.Builder(context).apply {
                            setTitle("Biometric Authentication")
                            setDescription("You must authenticate to create a Verifiable Presentation")
                            setAllowedAuthenticators(BIOMETRIC_STRONG)
                            setNegativeButton(
                                "Cancel",
                                ContextCompat.getMainExecutor(context)
                            ) { _, _ ->
                                Toast.makeText(context, "Cancelled", Toast.LENGTH_LONG).show()
                            }
                        }.build()
                        biometricPrompt.authenticate(CancellationSignal(),
                            ContextCompat.getMainExecutor(context),
                            PresentationBiometricCallback {
                                try {
                                    // Upon successful biometric authentication, create and send the VP
                                    viewModel.createAndSendVp(vpRequest.value!!)
                                    Toast.makeText(
                                        context,
                                        "Sending Presentation",
                                        Toast.LENGTH_LONG
                                    ).show()
                                } catch (e: Exception) {
                                    showErrorDialog(context, e)
                                }
                            }
                        )
                    } catch (e: Exception) {
                        showErrorDialog(context, e)
                    }
                },
                onDismiss = { showConfirmationDialog.value = false },
                vpRequest.value!!
            )
        }

    }


}