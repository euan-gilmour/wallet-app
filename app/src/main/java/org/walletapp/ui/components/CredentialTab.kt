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
import org.walletapp.viewmodels.CredentialViewModel

@Composable
fun CredentialTab(viewModel: CredentialViewModel) {
    val context = LocalContext.current
    val vc by viewModel.vc

    var vcInvitation = remember { mutableStateOf<VerifiableCredentialInvitation?>(null) }
    var showConfirmationDialog = remember { mutableStateOf(false) }

    val barcodeLauncher = rememberLauncherForActivityResult(
        contract = ScanContract()
    ) { result ->
        if (result?.contents == null) {
            Toast.makeText(context, "Cancelled", Toast.LENGTH_LONG).show()
        } else {
            vcInvitation.value = viewModel.extractVcInvitation(result.contents)
            showConfirmationDialog.value = true
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