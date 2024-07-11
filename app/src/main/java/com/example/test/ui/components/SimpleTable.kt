package com.example.test.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.test.domain.models.DestinationDomain

@Composable
fun SimpleTable(data: List<DestinationDomain?>) {
    val filterList = data.filter {
        it?.name != null && it.description != null && it.countryMode != null && it.type != null && it.picture != null && it.lastModify != null
    }
    val finalData = listOf(
        listOf("ID (int)", "Name (string)", "Description (string)", "CountryCode (string)", "Type (enum)", "LastModify (DateTime)"),
        filterList
    )

    LazyColumn {
        items(finalData.size) { rowIndex ->
            Row(modifier = Modifier.fillMaxWidth()) {
                for (colIndex in finalData[rowIndex].indices) {
                    Text(
                        text = finalData[rowIndex][colIndex].toString(),
                        modifier = Modifier
                            .weight(1f)
                            .padding(8.dp))
                }
            }
        }
    }
}