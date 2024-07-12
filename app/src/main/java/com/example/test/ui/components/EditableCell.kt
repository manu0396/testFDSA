package com.example.test.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp


// EditableCell.kt

@Composable
fun EditableCell(
    value: String,
    isEditable: Boolean,
    onValueChange: (String) -> Unit,
    onDoubleTap: () -> Unit,
    isSelected: Boolean,
    onDelete: () -> Unit // Modify onDelete parameter
) {

    Box(
        modifier = Modifier
            .padding(4.dp)
            .background(
                color = if (isSelected) Color.LightGray else Color.Transparent,
                shape = RoundedCornerShape(10.dp)
            )
            .clickable {
                if (isEditable) onDoubleTap()
            }
    ) {
        if (isEditable) {
            TextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier.fillMaxWidth()
            )
        } else {
            Row( // Use a Row to place Text and IconButton side by side
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween // Arrange items with space between
            ) {
                Text(text = value)
                IconButton(onClick = onDelete ) { // Delete button
                    Icon(Icons.Default.Delete, contentDescription = "Delete Cell")
                }
            }
        }
    }
}
