package com.example.test.ui.components

import android.content.Context
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.example.test.R
import com.example.test.SharedViewModel

@Composable
fun VerticalDataSelector(
    context: Context,
    modifier: Modifier = Modifier,
    data: List<String>,
    onItemSelected: (String) -> Unit,
    viewModel: SharedViewModel
) {
    var searchText by remember { mutableStateOf("") }

    Column(modifier = modifier) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            TextField(
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
                            val index = data.indexOf(searchText)
                            if (index >= 0) {
                                viewModel.setSelectedDestination(index)
                                onItemSelected(searchText)
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
                }
            )
        }
    }
}
