package com.example.test.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.ui.Alignment
import com.example.test.domain.models.DestinationDomain

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun EditableTable(
    data: List<DestinationDomain?>,
    onCellEdited: (rowIndex: Int, colIndex: Int, newValue: String) -> Unit,
    onCellDeleted: (rowIndex: Int, colIndex: Int) -> Unit,
    onCellSelected: (rowIndex: Int) -> Unit,
    selectedRowIndex: MutableState<Int?>,
    isModifyMode: Boolean,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isRefreshing by remember { mutableStateOf(false) }
    val pullRefreshState = rememberPullRefreshState(
        refreshing = isRefreshing,
        onRefresh = {
            isRefreshing = true
            onRefresh()
            isRefreshing = false
        }
    )
    val headers = listOf(
        "ID (int)",
        "Name (string)",
        "Description (string)",
        "CountryCode (string)",
        "Type (enum)",
        "Picture (string)",
        "LastModify (DateTime)"
    )
    Box(modifier = modifier.pullRefresh(pullRefreshState)) {
        Column(
            modifier = Modifier
                .horizontalScroll(rememberScrollState())
                .fillMaxWidth()
        ) {
            // Header row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp, horizontal = 4.dp)
            ) {

                headers.forEach { header ->
                    Box(
                        modifier = Modifier
                            .padding(4.dp)
                            .width(150.dp) // Adjust as needed for content
                    ) {
                        Text(
                            text = header,
                            fontSize = 12.sp,
                            color = Color.Black
                        )
                    }
                }
            }
            HorizontalDivider(thickness = 1.dp, color = Color.Gray)

            // Scrollable content for data rows within pull-to-refresh
            LazyColumn {
                itemsIndexed(data, key = { index, item -> item?.id ?: index.toString() }) { rowIndex, rowData ->
                    val isSelected = rowIndex == selectedRowIndex.value
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

                                Box(
                                    modifier = Modifier
                                        .padding(4.dp)
                                        .width(150.dp)
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
                                        Text(
                                            text = cellValue,
                                            modifier = Modifier.fillMaxWidth(),
                                            color = Color.Black
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

        PullRefreshIndicator(
            refreshing = isRefreshing,
            state = pullRefreshState,
            modifier = Modifier.align(Alignment.TopCenter)
        )
    }
}
