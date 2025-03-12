package org.walletapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import org.bouncycastle.jce.provider.BouncyCastleProvider
import org.walletapp.preferences.PreferencesManager
import org.walletapp.ui.components.CredentialTab
import org.walletapp.ui.components.DIDManagementTab
import org.walletapp.ui.components.KeyManagementTab
import org.walletapp.ui.components.PresentationTab
import org.walletapp.ui.theme.WalletAppTheme
import org.walletapp.viewmodels.CredentialViewModel
import org.walletapp.viewmodels.DIDViewModel
import org.walletapp.viewmodels.KeyViewModel
import org.walletapp.viewmodels.PresentationViewModel
import java.security.Security


data class NavigationItem(
    val title: String,
    val selectedIcon: ImageVector,
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        PreferencesManager.init(this)
        Security.removeProvider("BC")
        Security.addProvider(BouncyCastleProvider())
        enableEdgeToEdge()
        setContent {
            WalletAppTheme {
                val items = listOf(
                    NavigationItem("Keys", Icons.Default.Lock),
                    NavigationItem("DID", Icons.Default.AccountBox),
                    NavigationItem("Credential", Icons.Default.AccountCircle),
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
                        composable("DID") { DIDManagementTab(DIDViewModel()) }
                        composable("Credential") { CredentialTab(CredentialViewModel()) }
                        composable("Presentation") { PresentationTab(PresentationViewModel()) }
                    }
                }
            }
        }
    }
}