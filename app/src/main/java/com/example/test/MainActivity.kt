package com.example.test

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.example.test.ui.nav.Screens
import com.example.test.ui.screens.DestinationScreen
import com.example.test.ui.theme.TestTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setUpUi()
    }

    private fun setUpUi() {
        setContent {
            TestTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = Screens.Auth.route)
                {
                    navigation(startDestination = Screens.MainScreen.route, route = Screens.Auth.route) {
                        composable(Screens.MainScreen.route) { navBackStackEntry ->
                           DestinationScreen(
                               context = applicationContext,
                               viewModel = navBackStackEntry.sharedViewModel(navController = navController),
                               navController = navController
                           )
                        }
                        // Add new application modules as needed
                    }
                }
            }
        }
    }
}

/**
 * Function to instantiate viewmodel, passing route in case it exits.
 */
@Composable
fun NavBackStackEntry.sharedViewModel(navController: NavController): SharedViewModel {
    val navGraphRoute = destination.parent?.route ?: return hiltViewModel()
    val parentEntry = remember(this) {
        navController.getBackStackEntry(navGraphRoute)
    }
    return hiltViewModel(parentEntry)
}