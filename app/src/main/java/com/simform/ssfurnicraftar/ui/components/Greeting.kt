package com.simform.ssfurnicraftar.ui.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.simform.ssfurnicraftar.ui.theme.SSFurniCraftARTheme

@Composable
fun Greeting(
    modifier: Modifier = Modifier,
    name: String
) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    SSFurniCraftARTheme {
        Greeting(name = "Android")
    }
}