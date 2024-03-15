package com.simform.ssfurnicraftar.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.simform.ssfurnicraftar.navigation.AppNavHost
import com.simform.ssfurnicraftar.ui.components.Greeting

@Composable
fun SSFurniCraftARApp(
    appState: AppState = rememberAppState()
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Scaffold { padding ->
            Box(
                modifier = Modifier
                    .padding(padding)
            ) {
                AppNavHost(appState = appState)
            }
        }
    }
}
