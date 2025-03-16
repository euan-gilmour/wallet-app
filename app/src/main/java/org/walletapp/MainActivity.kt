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


/**
 * Represents a navigation item in the bottom navigation bar.
 */
data class NavigationItem(
    val title: String,
    val selectedIcon: ImageVector,
)

/**
 * Used for managing the routes for navigation
 */
object Routes {
    const val KEYS = "Keys"
    const val DID = "DID"
    const val CREDENTIAL = "Credential"
    const val PRESENTATION = "Presentation"
}

// Initialize the navigation items
private val navigationItems = listOf(
    NavigationItem(Routes.KEYS, Icons.Filled.Key),
    NavigationItem(Routes.DID, Icons.Filled.Person),
    NavigationItem(Routes.CREDENTIAL, Icons.Rounded.Badge),
    NavigationItem(Routes.PRESENTATION, Icons.Filled.MailLock)
)


/**
 * The main activity of the application, responsible for setting up the UI and navigation.
 *
 * Jetpack Compose is used to set up a bottom navigation bar and a navigation host
 * for switching between different tabs: Keys, DID, Credential, and Presentation.
 */
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize the preferences file for saving and loading data about DIDs, VCs, etc.
        PreferencesManager.init(this)

        // Enable edge-to-edge display
        enableEdgeToEdge()

        setContent {
            WalletAppTheme {

                // Set up a NavHostController
                val navController = rememberNavController()

                Scaffold(
                    bottomBar = {
                        NavigationBar {

                            // Get the current route from the controller
                            val currentRoute =
                                navController.currentBackStackEntryAsState().value?.destination?.route

                            // Iterate over the navigation items to create a NavigationBarItem for each
                            navigationItems.forEach { navigationItem ->
                                NavigationBarItem(
                                    selected = currentRoute == navigationItem.title,
                                    onClick = {
                                        navController.navigate(navigationItem.title) {
                                            launchSingleTop = true
                                            restoreState = true
                                        }
                                    },
                                    icon = {
                                        Icon(
                                            navigationItem.selectedIcon,
                                            contentDescription = navigationItem.title
                                        )
                                    },
                                    label = { Text(navigationItem.title) }
                                )
                            }
                        }
                    },
                ) { innerPadding ->
                    // Use NavHost to define the navigation graph and host the composable screens
                    NavHost(
                        navController = navController,
                        startDestination = "Keys",
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable(Routes.KEYS) { KeyManagementTab(KeyViewModel()) }
                        composable(Routes.DID) { DIDManagementTab(DIDViewModel()) }
                        composable(Routes.CREDENTIAL) { CredentialTab(CredentialViewModel()) }
                        composable(Routes.PRESENTATION) { PresentationTab(PresentationViewModel()) }
                    }
                }
            }
        }
    }
}