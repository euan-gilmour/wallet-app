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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions
import org.walletapp.viewmodels.CredentialViewModel

@Composable
fun CredentialTab(viewModel: CredentialViewModel) {
    val context = LocalContext.current
    val vc by viewModel.vc

    val barcodeLauncher = rememberLauncherForActivityResult(
        contract = ScanContract()
    ) { result ->
        if (result?.contents == null) {
            Toast.makeText(context, "Cancelled", Toast.LENGTH_LONG).show()
        } else {
            viewModel.initiateCredentialTransfer(result.contents)
        }
    }

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

        Button(onClick = { barcodeLauncher.launch(ScanOptions().setOrientationLocked(false)) }) {
            Text("Scan VC Invitation")
        }

        Spacer(Modifier.height(16.dp))

        Button(onClick = { viewModel.deleteVc() }) {
            Text("Delete VC")
        }
    }
}