package com.example.test.ui.components

import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp


@Composable
fun VerticalDataSelector(
    modifier: Modifier,
    data: List<String>,
    onItemSelected: (String) -> Unit,
) {
    var selectedItem by remember { mutableStateOf("") }
    var searchText by remember { mutableStateOf("") }
    val filteredData = if (searchText.isBlank()) {
        data
    } else {
        data.filter { it.contains(searchText, ignoreCase = true) }
    }
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp

    Column(
        modifier = modifier
            .width(screenWidth / 5)
            .fillMaxHeight()
    ) {
        LazyColumn(
            modifier = Modifier
                .weight(1f)
        ) {
            items(filteredData.size) { index ->
                val item = filteredData[index]
                Text(
                    text = item,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            selectedItem = item
                            onItemSelected(item)
                        }
                        .padding(8.dp),
                    textAlign = TextAlign.Center
                )
            }
        }
    }

    FloatingActionButton(
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = Color.White,
        onClick = { /* Handle search action */ },
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Text("Search")
    }
}
