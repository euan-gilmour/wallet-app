package org.walletapp.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import org.walletapp.viewmodels.DIDViewModel

/**
 * A composable function for the DID Management tab.
 *
 * This screen allows the user to enter a web domain for a did:web DID,
 * create a DID document for that domain, and copy that DID document to the device clipboard.
 * It also allows deleting the current DID and DID document.
 *
 * @param viewModel The view model providing the data and logic for the DID Management tab.
 */
@Composable
fun DIDManagementTab(viewModel: DIDViewModel) {
    // Get observed values from the viewModel
    val did by viewModel.did
    val didDocument by viewModel.didDocument
    val domain by viewModel.domain

    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Display DID
        Text(text = "DID: $did")

        Spacer(modifier = Modifier.height(16.dp))

        // Display DID Document
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            Text(
                text = "DID Document:\n\n$didDocument",
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .padding(8.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Input field for the web domain to create a DID for
        TextField(
            value = domain,
            onValueChange = { viewModel.domainChanged(it) },
            label = { Text("Enter your web domain using ':' for paths (e.g. example.com:path:to:directory)") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Uri,
                imeAction = ImeAction.Done
            ),
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Create a DID and DID document based on the domain field
        Button(onClick = {
            try {
                viewModel.createDid()
            } catch (e: Exception) {
                showErrorDialog(context, e)
            }
        }) {
            Text("Create DID")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Copy the DID document to the device clipboard
        Button(onClick = { viewModel.copyDID(context) }) {
            Text("Copy DID Document")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Delete the current DID and DID document
        Button(onClick = { viewModel.deleteDid() }) {
            Text("Delete DID")
        }
    }
}