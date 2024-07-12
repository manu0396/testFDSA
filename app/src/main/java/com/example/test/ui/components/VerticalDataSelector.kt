package com.example.test.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.test.R
import com.example.test.SharedViewModel
import java.time.format.TextStyle

@Composable
fun VerticalDataSelector(
    modifier: Modifier = Modifier,
    data: List<String>,
    onItemSelected: (String) -> Unit,
    viewModel: SharedViewModel
) {
    var selectedItem by remember { mutableStateOf("") }
    var searchText by remember { mutableStateOf("") }

    Column(modifier = modifier) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            IconButton(
                onClick = {
                    val index = data.indexOf(searchText)
                    if (index >= 0) {
                        viewModel.setSelectedDestination(index)
                        onItemSelected(searchText) // Notify the parent about the selection
                    } else {
                        // Handle case where item is not found, maybe show a Toast
                    }
                },
                modifier = Modifier.size(48.dp)
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.baseline_arrow_forward_24),
                    contentDescription = "Search icon",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }

            TextField(
                value = searchText,
                onValueChange = { searchText = it },
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 8.dp),
                textStyle = androidx.compose.ui.text.TextStyle(fontSize = 16.sp),
                placeholder = {
                    Text("Search...")
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        // You might want to trigger the search here as well
                    }
                )
            )
        }
    }
}