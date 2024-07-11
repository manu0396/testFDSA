package com.example.test.ui.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@Composable
fun H2Text(text: String, modifier: Modifier) {
    Text(
        text = text,
        modifier = modifier,
        style = TextStyle(
            fontSize = 18.sp, // Typical H2 font size
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.1.sp // Optional: Add letter spacing
        )
    )
}