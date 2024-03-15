package com.simform.ssfurnicraftar.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.simform.ssfurnicraftar.navigation.AppNavHost
import com.simform.ssfurnicraftar.ui.theme.SSFurniCraftARTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            SSFurniCraftARTheme {
                SSFurniCraftARApp()
            }
        }
    }
}
