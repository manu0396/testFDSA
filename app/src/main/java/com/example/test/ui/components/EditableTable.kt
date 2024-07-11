package com.example.test.ui.components

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.test.domain.models.DestinationDomain

@Composable
fun EditableTable(
    data: List<DestinationDomain?>,
    onCellEdited: (rowIndex: Int, colIndex: Int, newValue: String) -> Unit,
    onCellDeleted: (rowIndex: Int, colIndex: Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val filterList = data.filter {
        it?.name != null && it.description != null && it.countryMode != null && it.type != null && it.picture != null && it.lastModify != null
    }
    val finalData = listOf(
        listOf(
            "ID (int)",
            "Name (string)",
            "Description (string)",
            "CountryCode (string)",
            "Type (enum)",
            "Picture (string)",
            "LastModify (DateTime)"
        )
    ) + filterList.map {
        listOf(
            it?.id,
            it?.name,
            it?.description,
            it?.countryMode,
            it?.type,
            it?.picture,
            it?.lastModify.toString()
        )
    }

    LazyColumn(modifier = modifier) {
        items(finalData.size) { rowIndex ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                for (colIndex in finalData[rowIndex].indices) {
                    var cellValue by remember { mutableStateOf(finalData[rowIndex][colIndex].toString()) }
                    var showDeleteConfirmation by remember { mutableStateOf(false) }

                    if (showDeleteConfirmation) {
                        AlertDialog(
                            onDismissRequest = { showDeleteConfirmation = false },
                            title = { Text("Confirm Deletion") },
                            text = { Text("Are you sure you want to delete this cell?") },
                            confirmButton = {
                                Button(onClick = {
                                    onCellDeleted(rowIndex, colIndex)
                                    showDeleteConfirmation = false
                                }) {
                                    Text("Delete")
                                }
                            },
                            dismissButton = {
                                Button(onClick = { showDeleteConfirmation = false }) {
                                    Text("Cancel")
                                }
                            }
                        )
                    }

                    CustomTextField(
                        value = cellValue,
                        onValueChange = { newValue: String ->
                            cellValue = newValue
                            onCellEdited(rowIndex, colIndex, newValue)
                        },
                        modifier = Modifier
                            .weight(1f)
                            .padding(4.dp) // Reduce padding for closer cells
                            .pointerInput(Unit) {
                                detectTapGestures(
                                    onLongPress = {
                                        showDeleteConfirmation = true
                                    }
                                )
                            },
                        fontSize = 8.sp
                    )
                }
            }
        }
    }
}

