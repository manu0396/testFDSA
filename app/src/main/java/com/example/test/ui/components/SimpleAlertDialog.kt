package com.example.test.ui.components

import android.content.Context
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.example.test.R

@Composable
fun SimpleAlertDialog(
    context: Context,
    show: Boolean,
    title: String,
    text: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    elevation: Int,
) {
    if (show) {
        AlertDialog(
            onDismissRequest = onDismiss,
            confirmButton = {
                TextButton(onClick = onConfirm) {
                    Text(context.getString(R.string.ok))
                }
            },
            dismissButton = {
                TextButton(onClick = onDismiss) {
                    Text(context.getString(R.string.cancel))
                }
            },
            title = { Text(title) },
            text = { Text(text) },
            tonalElevation = elevation.dp
        )
    }
}