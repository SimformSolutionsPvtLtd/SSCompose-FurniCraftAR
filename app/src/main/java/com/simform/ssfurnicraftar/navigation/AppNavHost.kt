package com.simform.ssfurnicraftar.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.simform.ssfurnicraftar.ui.AppState
import com.simform.ssfurnicraftar.ui.components.Greeting

const val GREETING_ROUTE = "greeting"

@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    appState: AppState,
    startDestination: String = GREETING_ROUTE,
    onShowSnackbar: suspend (String, String?) -> Boolean
) {

    val navController = appState.navController

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier,
    ) {
        composable(
            route = GREETING_ROUTE,
        ) {
            Greeting(name = "World")
        }
    }
}
