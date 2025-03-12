package org.walletapp.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.walletapp.KeyViewModel

@Composable
fun KeyManagementTab(viewModel: KeyViewModel) {
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
        Text(text = "Key Status: $keyStatus")

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Public Key: $publicKey")

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Key Security Level: $keySecurityLevel")

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { viewModel.generateKey() }) {
            Text("Generate Key")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { viewModel.checkKey() }) {
            Text("Check Key Status")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { viewModel.deleteKey() }) {
            Text("Delete Key")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { viewModel.getSecurityLevel() }) {
            Text("Check Security Level")
        }

    }
}