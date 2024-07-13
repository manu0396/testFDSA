package com.example.test.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.test.domain.models.DestinationDomain

@Composable
fun EditableTable(
    data: List<DestinationDomain?>,
    onCellEdited: (rowIndex: Int, colIndex: Int, newValue: String) -> Unit,
    onCellDeleted: (rowIndex: Int, colIndex: Int) -> Unit,
    onCellSelected: (rowIndex: Int) -> Unit,
    selectedRowIndex: MutableState<Int?>,
    isModifyMode: Boolean,
    modifier: Modifier = Modifier
) {
    val headers = listOf(
        "ID (int)",
        "Name (string)",
        "Description (string)",
        "CountryCode (string)",
        "Type (enum)",
        "Picture (string)",
        "LastModify (DateTime)"
    )

    LazyColumn(modifier = modifier) {
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp, horizontal = 4.dp)
            ) {
                headers.forEachIndexed { index, header ->
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .padding(4.dp)
                    ) {
                        Text(
                            text = header,
                            fontSize = 12.sp
                        )
                    }
                }
            }
            HorizontalDivider(thickness = 1.dp, color = Color.Gray)
        }

        itemsIndexed(data) { rowIndex, rowData ->
            val isSelected = rowIndex == selectedRowIndex.value // Check if the current row is selected
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(if (isSelected) Color.LightGray.copy(alpha = 0.3f) else Color.Transparent)
                    .clickable {
                        if (!isModifyMode) {
                            onCellSelected(rowIndex)
                        }
                    }
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp, horizontal = 4.dp)
                ) {
                    listOf(
                        rowData?.id ?: "",
                        rowData?.name ?: "",
                        rowData?.description ?: "",
                        rowData?.countryMode ?: "",
                        rowData?.type ?: "",
                        rowData?.picture ?: "",
                        rowData?.lastModify.toString()
                    ).forEachIndexed { colIndex, value ->
                        var cellValue by remember { mutableStateOf(value) }

                        // Enable editing only if isModifyMode is true
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .padding(4.dp)
                        ) {
                            if (isModifyMode) {
                                TextField(
                                    value = cellValue,
                                    onValueChange = { newValue ->
                                        cellValue = newValue
                                        onCellEdited(rowIndex, colIndex, newValue)
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .pointerInput(Unit) {
                                            detectTapGestures(onDoubleTap = {
                                                onCellDeleted(rowIndex, colIndex)
                                            })
                                        }
                                )
                            } else {
                                // Display non-editable text
                                Text(
                                    text = cellValue,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }
                        if (colIndex < headers.size - 1) {
                            Divider(
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .width(1.dp),
                                color = Color.Gray
                            )
                        }
                    }
                }
                Divider(color = Color.Gray, thickness = 1.dp)
            }
        }
    }
}
