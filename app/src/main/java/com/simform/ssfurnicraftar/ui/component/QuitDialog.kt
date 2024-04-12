package com.simform.ssfurnicraftar.ui.component

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.simform.ssfurnicraftar.R

@Composable
fun QuitDialog(
    modifier: Modifier = Modifier,
    message: String,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        modifier = modifier,
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(onClick = onConfirm) {
                Text(text = stringResource(id = R.string.confirm))
            }
        },
        text = {
            Text(
                text = message,
                style = MaterialTheme.typography.titleMedium
            )
        }
    )
}
