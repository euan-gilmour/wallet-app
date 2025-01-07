package org.walletapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import org.walletapp.ui.theme.WalletAppTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WalletAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    KeyManagementTab(KeyViewModel())
                }
            }
        }
    }
}

@Composable
fun KeyManagementTab(viewModel: KeyViewModel) {
    val keyStatus by viewModel.keyStatus
    val publicKey by viewModel.publicKey
    val keySecurityLevel by viewModel.keySecurityLevel

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
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