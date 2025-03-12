package org.walletapp.ui.components

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
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions
import org.walletapp.data.VerifiablePresentationRequest
import org.walletapp.viewmodels.PresentationViewModel

@Composable
fun PresentationTab(viewModel: PresentationViewModel) {
    val context = LocalContext.current

    var vpRequest = remember { mutableStateOf<VerifiablePresentationRequest?>(null) }
    var showConfirmationDialog = remember { mutableStateOf(false) }

    val barcodeLauncher = rememberLauncherForActivityResult(
        contract = ScanContract()
    ) { result ->
        if (result?.contents == null) {
            Toast.makeText(context, "Cancelled", Toast.LENGTH_LONG).show()
        } else {
            vpRequest.value = viewModel.extractVpRequest(result.contents)
            showConfirmationDialog.value = true
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Button(onClick = {
            barcodeLauncher.launch(ScanOptions().setOrientationLocked(false))
        }) {
            Text("Scan VP Request")
        }

        if (showConfirmationDialog.value && vpRequest.value != null) {
            PresentationConfirmationDialog(
                onProceed = {
                    viewModel.createAndSendVp(context, vpRequest.value!!)
                    showConfirmationDialog.value = false
                },
                onDismiss = { showConfirmationDialog.value = false },
                vpRequest.value!!
            )
        }

    }


}