package org.walletapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.MailLock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.rounded.Badge
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import org.walletapp.managers.PreferencesManager
import org.walletapp.ui.components.CredentialTab
import org.walletapp.ui.components.DIDManagementTab
import org.walletapp.ui.components.KeyManagementTab
import org.walletapp.ui.components.PresentationTab
import org.walletapp.ui.theme.WalletAppTheme
import org.walletapp.viewmodels.CredentialViewModel
import org.walletapp.viewmodels.DIDViewModel
import org.walletapp.viewmodels.KeyViewModel
import org.walletapp.viewmodels.PresentationViewModel

data class NavigationItem(
    val title: String,
    val selectedIcon: ImageVector,
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        PreferencesManager.init(this)
        enableEdgeToEdge()
        setContent {
            WalletAppTheme {
                val items = listOf(
                    NavigationItem("Keys", Icons.Filled.Key),
                    NavigationItem("DID", Icons.Filled.Person),
                    NavigationItem("Credential", Icons.Rounded.Badge),
                    NavigationItem("Presentation", Icons.Filled.MailLock)
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
                        startDestination = "Keys",
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable("Keys") { KeyManagementTab(KeyViewModel()) }
                        composable("DID") { DIDManagementTab(DIDViewModel()) }
                        composable("Credential") { CredentialTab(CredentialViewModel()) }
                        composable("Presentation") { PresentationTab(PresentationViewModel()) }
                    }
                }
            }
        }
    }
}