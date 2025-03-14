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
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import org.walletapp.viewmodels.DIDViewModel

@Composable
fun DIDManagementTab(viewModel: DIDViewModel) {
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
        Text(text = "DID: $did")

        Spacer(modifier = Modifier.height(16.dp))

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

        TextField(
            value = domain,
            onValueChange = { viewModel.domainChanged(it) },
            label = { Text("Enter your web domain using ':' for paths (e.g. example.com:path:to:directory)") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { try {
            viewModel.createDid()
        } catch (e: Exception) {
            showErrorDialog(context, e)
        } }) {
            Text("Create DID")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { viewModel.copyDID(context) }) {
            Text("Copy DID Document")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { viewModel.deleteDid() }) {
            Text("Delete DID")
        }
    }
}