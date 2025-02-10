package org.walletapp

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions
import org.walletapp.ui.theme.WalletAppTheme


data class NavigationItem(
    val title: String,
    val selectedIcon: ImageVector,
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WalletAppTheme {
                val items = listOf(
                    NavigationItem("Key Management", Icons.Default.Lock),
                    NavigationItem("DID Management", Icons.Default.AccountBox),
                    NavigationItem("Presentation", Icons.Default.Create)
                )
                val navController = rememberNavController()

                Scaffold(
                    bottomBar = {
                        NavigationBar {
                            val currentRoute =
                                navController.currentBackStackEntryAsState().value?.destination?.route

                            items.forEach { item ->
                                NavigationBarItem(
                                    selected = currentRoute == item.title,
                                    onClick = {
                                        navController.navigate(item.title) {
                                            launchSingleTop = true
                                            restoreState = true
                                        }
                                    },
                                    icon = {
                                        Icon(
                                            item.selectedIcon,
                                            contentDescription = item.title
                                        )
                                    },
                                    label = { Text(item.title) }
                                )
                            }
                        }
                    },
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = "Key Management",
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable("Key Management") { KeyManagementTab(KeyViewModel()) }
                        composable("DID Management") { DIDManagementTab(DIDViewModel()) }
                        composable("Presentation") { PresentationTab(PresentationViewModel()) }
                    }
                }
            }
        }
    }
}

sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
    data object KeyManagement : Screen("key_management", "Keys", Icons.Default.Lock)
    data object DID : Screen("did_management", "Settings", Icons.Default.AccountBox)
}

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

@Composable
fun DIDManagementTab(viewModel: DIDViewModel) {
    val did by viewModel.did
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
        Text(text = "DID Document:\n$did")

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = domain,
            onValueChange = { viewModel.domainChanged(it) },
            label = { Text("Enter your did:web domain (e.g. example.com:subdirectory") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { viewModel.createDid() }) {
            Text("Create DID")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { viewModel.copyDID(context) }) {
            Text("Copy DID")
        }
    }
}

@Composable
fun PresentationTab(viewModel: PresentationViewModel) {
    val context = LocalContext.current
    val vp by viewModel.vp
    val verificationStatus by viewModel.verificationStatus

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
                text = "Verifiable Presentation:\n\n$vp",
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .padding(8.dp)
            )
        }

        Spacer(Modifier.height(16.dp))

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 100.dp)
        ) {
            Text(
                text = "Verification Status: $verificationStatus",
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .padding(8.dp)
            )
        }

        Spacer(Modifier.height(16.dp))

        Button(onClick = { viewModel.generatePresentation() }) {
            Text("Generate Presentation")
        }

        Spacer(Modifier.height(16.dp))

        Button(onClick = { viewModel.verifyPresentation() }) {
            Text("Verify Presentation")
        }
    }
}