package com.simform.ssfurnicraftar.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.simform.ssfurnicraftar.data.utils.NetworkMonitor
import com.simform.ssfurnicraftar.ui.theme.SSFurniCraftARTheme
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var networkMonitor: NetworkMonitor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val appState = rememberAppState(
                networkMonitor = networkMonitor
            )

            SSFurniCraftARTheme {
                SSFurniCraftARApp(appState)
            }
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        Timber.d("intent: ${intent == null}")
    }
}
