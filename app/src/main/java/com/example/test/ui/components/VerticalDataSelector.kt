package com.example.test.ui.components

import android.content.Context
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.example.test.R
import com.example.test.SharedViewModel
import java.util.*

@Composable
fun VerticalDataSelector(
    context: Context,
    modifier: Modifier = Modifier,
    data: List<String>,
    onItemSelected: (String) -> Unit,
    onRowSelected: (Int, String?) -> Unit,
    viewModel: SharedViewModel
) {
    var searchText by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primaryContainer)
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            OutlinedTextField(
                value = searchText,
                onValueChange = { searchText = it },
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp),
                textStyle = MaterialTheme.typography.bodyMedium,
                placeholder = {
                    Text("Search...")
                },
                singleLine = true,
                leadingIcon = {
                    IconButton(
                        onClick = {
                            Log.d("VerticalDataSelector", "Search button clicked")
                            Log.d("VerticalDataSelector", "SearchText: $searchText")

                            // Convert searchText to lowercase
                            val searchTextLower = searchText.lowercase(Locale.ROOT).trim()

                            // Find the index of the item that contains searchTextLower in its name (case-insensitive)
                            val index = data.indexOfFirst { item ->
                                item.lowercase(Locale.ROOT).contains(searchTextLower)
                            }

                            if (index >= 0) {
                                viewModel.setSelectedDestination(index)
                                onItemSelected(data[index]) // Pass the original data item
                                onRowSelected(index, context.getString(R.string.searchItemFound, data[index]))
                            } else {
                                viewModel.showError(context.getString(R.string.no_data_found))
                            }
                        }
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.baseline_manage_search_24),
                            contentDescription = "Search icon"
                        )
                    }
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                    focusedTextColor = MaterialTheme.colorScheme.onSurface,
                    unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                    cursorColor = MaterialTheme.colorScheme.onSurface,
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                    focusedLeadingIconColor = MaterialTheme.colorScheme.onSurface,
                    unfocusedLeadingIconColor = MaterialTheme.colorScheme.onSurface,
                    focusedPlaceholderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                    unfocusedPlaceholderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                    focusedLabelColor = Color.Transparent,
                    unfocusedLabelColor = Color.Transparent
                )
            )
        }
    }
}
