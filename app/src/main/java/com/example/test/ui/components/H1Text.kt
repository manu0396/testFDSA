package com.example.test.ui.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@Composable
fun H1Text(
    text: String,
    modifier: Modifier
    ) {
    Text(
        modifier = modifier,
        text = text,
        style = TextStyle(
            fontSize = 24.sp, // Adjust font size as needed
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.15.sp // Optional: Add letter spacing for a more "heading" look
        )
    )
}