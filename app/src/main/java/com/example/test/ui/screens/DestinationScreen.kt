package com.example.test.ui.screens

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.test.R
import com.example.test.SharedViewModel
import com.example.test.data.models.Timestamp
import com.example.test.ui.components.AppBar
import com.example.test.ui.components.EditableTable
import com.example.test.ui.components.H1Text
import com.example.test.ui.components.H2Text
import com.example.test.ui.components.SimpleAlertDialog
import com.example.test.ui.components.VerticalDataSelector
import com.example.test.ui.theme.TestTheme
import com.example.test.utils.NetworkUtils

@Composable
fun DestinationScreen(
    context: Context,
    viewModel: SharedViewModel,
    navController: NavController,
) {
    val data by viewModel.data.collectAsState()
    LaunchedEffect(key1 = data) {
        viewModel.getLocalData(context)
        viewModel.getResults(context)
        checkConnectivity(context)
    }
    val localData by viewModel.localData.collectAsState()
    val showLoading by viewModel.showLoading.collectAsState()
    val showDialog by viewModel.showDialog.collectAsState()
    val messageDialog by viewModel.messageDialog.collectAsState()

    val filterData = if(checkConnectivity(context)){
        data
    }else{
        localData
    }

    val currentText = remember { mutableStateOf("") }

    TestTheme {
        if (showDialog) {
            SimpleAlertDialog(
                context = context,
                show = true,
                title = context.getString(R.string.titleError),
                text = messageDialog,
                onConfirm = viewModel::onDialogConfirm,
                onDismiss = viewModel::onDialogDismiss,
                elevation = 10
            )
        }

        Scaffold(
            topBar = {
                AppBar(
                    title = context.getString(R.string.main_title),
                    onBackClick = { navController.popBackStack() }
                )
            },
            contentWindowInsets = WindowInsets(16.dp, 16.dp, 16.dp, 16.dp)
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                // Title
                H1Text(
                    text = context.getString(R.string.destination_title),
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(16.dp)
                )

                // Table or Loading/Error
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 64.dp, bottom = 200.dp), // Adjust padding as needed
                    contentAlignment = Alignment.Center
                ) {
                    if (showLoading) {
                        CircularProgressIndicator()
                    } else {
                        if (filterData.isEmpty()) {
                            H2Text(
                                text = context.getString(R.string.no_data_found),
                                modifier = Modifier.align(Alignment.Center)
                            )
                        } else {
                            EditableTable(
                                data = filterData,
                                modifier = Modifier.align(Alignment.Center),
                                onCellEdited = { rowIndex, colIndex, newValue ->
                                    viewModel.data.value[rowIndex]?.let { destination ->
                                        val updatedDestination = when (colIndex) {
                                            0 -> destination.copy(id = newValue)
                                            1 -> destination.copy(name = newValue)
                                            2 -> destination.copy(description = newValue)
                                            3 -> destination.copy(countryMode = newValue)
                                            4 -> destination.copy(type = newValue)
                                            5 -> destination.copy(picture = newValue)
                                            6 -> destination.copy(lastModify = Timestamp(newValue.toLong()))
                                            else -> destination
                                        }
                                        viewModel.updateData(rowIndex, updatedDestination)
                                    }
                                },
                                onCellDeleted = { rowIndex, colIndex ->
                                    viewModel.data.value[rowIndex]?.let { destination ->
                                        val updatedDestination = when (colIndex) {
                                            0 -> destination.copy(id = null)
                                            1 -> destination.copy(name = null)
                                            2 -> destination.copy(description = null)
                                            3 -> destination.copy(countryMode = null)
                                            4 -> destination.copy(type = null)
                                            5 -> destination.copy(picture = null)
                                            6 -> destination.copy(lastModify = null)
                                            else -> destination
                                        }
                                        viewModel.updateData(rowIndex, updatedDestination)
                                    }
                                }
                            )
                        }
                    }
                }

                // Button Container
                Row(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    FloatingActionButton(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = Color.White,
                        onClick = {
                            Toast.makeText(
                                context, "Destiny created", Toast.LENGTH_SHORT
                            ).show()
                        },
                        shape = RoundedCornerShape(10),
                        modifier = Modifier
                            .sizeIn(minWidth = 60.dp, minHeight = 60.dp)
                    ) {
                        Text(text = context.getString(R.string.createDestiny))
                    }
                    Spacer(modifier = Modifier.width(16.dp)) // Add space between buttons
                    FloatingActionButton(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = Color.White,
                        onClick = {
                            Toast.makeText(
                                context, "Destiny modify", Toast.LENGTH_SHORT
                            ).show()
                        },
                        shape = RoundedCornerShape(10),
                        modifier = Modifier
                            .sizeIn(minWidth = 60.dp, minHeight = 60.dp)
                    ) {
                        Text(text = context.getString(R.string.modifyDestiny))
                    }
                    Spacer(modifier = Modifier.width(16.dp)) // Add space between buttons
                    FloatingActionButton(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = Color.White,
                        onClick = {
                            Toast.makeText(
                                context, "Destiny deleted", Toast.LENGTH_SHORT
                            ).show()
                        },
                        shape = RoundedCornerShape(10),
                        modifier = Modifier
                            .sizeIn(minWidth = 60.dp, minHeight = 60.dp)
                    ) {
                        Text(text = context.getString(R.string.removeDestiny))
                    }
                }

                // Vertical Data Selector
                VerticalDataSelector(
                    data = data.mapNotNull { it?.id },
                    onItemSelected = {
                        Toast.makeText(
                            context, "Selected item: $it", Toast.LENGTH_SHORT
                        ).show()
                    },
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .padding(16.dp)
                )
            }
        }
    }
}

fun checkConnectivity(context: Context): Boolean {
    return when {
        NetworkUtils.isConnectedToWifi(context) -> {
            // Device is connected to Wi-Fi
            true
        }
        NetworkUtils.isConnectedToCellular(context) -> {
            // Device is connected to cellular data (4G/LTE, etc.)
            true
        }
        NetworkUtils.isOffline(context) -> {
            // Device is offline
            false
        }
        else -> false
    }
}
