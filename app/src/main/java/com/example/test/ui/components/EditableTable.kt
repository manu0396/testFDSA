package com.example.test.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
                    .background(color = Color.LightGray)
                    .padding(vertical = 8.dp, horizontal = 4.dp)
            ) {
                headers.forEachIndexed { index, header ->
                    Text(
                        text = header,
                        modifier = Modifier
                            .weight(1f)
                            .padding(4.dp),
                        fontSize = 12.sp
                    )
                }
            }
        }

        items(data.size) { rowIndex ->
            val isSelected = rowIndex == selectedRowIndex.value
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp, horizontal = 4.dp)
                    .background(if (isSelected) Color.LightGray else Color.Transparent)
                    .clickable {
                        if (!isModifyMode) {
                            onCellSelected(rowIndex)
                        }
                    }
            ) {
                val rowData = data[rowIndex]
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
                    if (isModifyMode) {
                        TextField(
                            value = cellValue,
                            onValueChange = { newValue ->
                                cellValue = newValue
                                onCellEdited(rowIndex, colIndex, newValue)
                            },
                            modifier = Modifier
                                .weight(1f)
                                .padding(4.dp)
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
                            modifier = Modifier
                                .weight(1f)
                                .padding(4.dp)
                                .fillMaxWidth()
                        )
                    }
                }
            }
        }
    }
}
