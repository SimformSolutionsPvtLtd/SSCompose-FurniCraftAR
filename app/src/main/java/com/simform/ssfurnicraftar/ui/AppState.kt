package com.simform.ssfurnicraftar.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.CoroutineScope

@Composable
fun rememberAppState(
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    navController: NavHostController = rememberNavController()
): AppState {
    return remember(
        navController,
        coroutineScope
    ) {
        AppState(
            navController,
            coroutineScope
        )
    }
}

class AppState(
    val navController: NavHostController,
    val coroutineScope: CoroutineScope,
) {

}