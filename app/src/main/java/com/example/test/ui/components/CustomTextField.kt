package com.example.test.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CustomTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    fontSize: TextUnit = 16.sp
) {
    var text by remember { mutableStateOf(value) }

    BasicTextField(
        value = text,
        onValueChange = {
            text = it
            onValueChange(it)
        },
        modifier = modifier
            .background(Color.White)
            .border(1.dp, Color.Gray)
            .padding(8.dp),
        textStyle = TextStyle(
            color = Color.Black,
            fontSize = fontSize,
            fontWeight = FontWeight.Normal
        )
    )
}