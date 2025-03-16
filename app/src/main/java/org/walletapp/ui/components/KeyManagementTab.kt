package org.walletapp.ui.components

import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import org.walletapp.viewmodels.KeyViewModel

/**
 * A composable function for the Key Management tab.
 *
 * This screen allows the user to generate a new keypair, delete the current keypair,
 * and view the status, public key, and security level of the keypair.
 *
 * @param viewModel The view model providing the data and logic for the Key Management tab.
 */
@Composable
fun KeyManagementTab(viewModel: KeyViewModel) {
    val context = LocalContext.current

    // Get observed values from the viewModel
    val keyStatus by viewModel.keyStatus
    val publicKey by viewModel.publicKey
    val keySecurityLevel by viewModel.keySecurityLevel

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Display key status
        Text(text = "Key Status: $keyStatus")

        Spacer(modifier = Modifier.height(16.dp))

        // Display key security level
        Text(text = "Key Security Level: $keySecurityLevel")

        Spacer(modifier = Modifier.height(16.dp))

        // Display public key
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = "Public Key:\n\n$publicKey",
                modifier = Modifier
                    .padding(8.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Generate a new keypair
        Button(onClick = {
            try {
                viewModel.generateKey()
            } catch (e: Exception) {
                showErrorDialog(context, e)
            }
        }) {
            Text("Generate Key")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Delete the current keypair
        Button(onClick = { viewModel.deleteKey() }) {
            Text("Delete Key")
        }

    }
}