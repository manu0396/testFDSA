package com.example.test.ui.screens

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.test.R
import com.example.test.SharedViewModel
import com.example.test.data.models.Timestamp
import com.example.test.domain.models.DestinationDomain
import com.example.test.ui.components.AppBar
import com.example.test.ui.components.EditableTable
import com.example.test.ui.components.H1Text
import com.example.test.ui.components.H2Text
import com.example.test.ui.components.SimpleAlertDialog
import com.example.test.ui.theme.TestTheme
import com.example.test.utils.NetworkUtils

@Composable
fun DestinationScreen(
    context: Context,
    viewModel: SharedViewModel,
    navController: NavController,
) {
    // Collect state from ViewModel
    val data by viewModel.data.collectAsState()
    val localData by viewModel.localData.collectAsState()
    val showLoading by viewModel.showLoading.collectAsState()
    val showDialog by viewModel.showDialog.collectAsState()
    val messageDialog by viewModel.messageDialog.collectAsState()

    // Check connectivity and determine which data to display
    val filterData = if (checkConnectivity(context)) {
        data
    } else {
        localData
    }

    // State for managing selected row index
    val selectedRowIndex = remember { mutableStateOf<Int?>(null) }

    // State for managing create and modify dialogs
    var showDialogCreate by remember { mutableStateOf(false) }
    var showDialogModify by remember { mutableStateOf(false) }
    var createDestinationName by remember { mutableStateOf("") }
    var createDestinationDescription by remember { mutableStateOf("") }

    // Launched effect to trigger initial data fetching
    LaunchedEffect(key1 = data) {
        viewModel.getLocalData(context)
        viewModel.getResults(context)
        checkConnectivity(context)
    }

    // Scaffold and UI components
    TestTheme {
        // Show dialog for errors if showDialog is true
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

        // Main Scaffold with app bar and content
        Scaffold(
            topBar = {
                AppBar(
                    title = context.getString(R.string.main_title),
                    onBackClick = { navController.popBackStack() }
                )
            }
        ) { paddingValues ->
            // Box to contain main content
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

                // Loading or Error message when data is loading or empty
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 64.dp, bottom = 200.dp),
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
                            // Display EditableTable with data
                            // Inside EditableTable composable function
                            EditableTable(
                                data = filterData,
                                modifier = Modifier.align(Alignment.Center),
                                onCellEdited = { rowIndex, colIndex, newValue ->
                                    viewModel.data.value[rowIndex]?.let { destination ->
                                        val updatedDestination = when (colIndex) {
                                            // Handle numeric fields (like lastModify as Long)
                                            0 -> destination.copy(id = newValue)
                                            1 -> destination.copy(name = newValue)
                                            2 -> destination.copy(description = newValue)
                                            3 -> destination.copy(countryMode = newValue)
                                            4 -> destination.copy(type = newValue)
                                            5 -> destination.copy(picture = newValue)
                                            6 -> {
                                                val timestampValue = if (newValue.isNotEmpty()) newValue.toLong() else 0L
                                                destination.copy(lastModify = Timestamp(timestampValue))
                                            }
                                            else -> destination
                                        }
                                        viewModel.updateData(rowIndex, updatedDestination)
                                    }
                                },
                                onCellDeleted = { rowIndex, colIndex ->
                                    viewModel.data.value[rowIndex]?.let { destination ->
                                        val updatedDestination = when (colIndex) {
                                            // Handle numeric fields (like lastModify as Long)
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
                                },
                                onCellSelected = { rowIndex ->
                                    selectedRowIndex.value = rowIndex
                                },
                                selectedRowIndex = selectedRowIndex
                            )
                        }
                    }
                }

                // Row of action buttons (Create, Modify, Delete)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp)
                        .align(Alignment.BottomCenter),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Create button
                    FloatingActionButton(
                        onClick = {
                            showDialogCreate = true
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Create")
                    }

                    // Modify button
                    FloatingActionButton(
                        onClick = {
                            selectedRowIndex.value?.let { rowIndex ->
                                showDialogModify = true
                                val selectedDestination = filterData[rowIndex]
                                createDestinationName = selectedDestination?.name ?: ""
                                createDestinationDescription = selectedDestination?.description ?: ""
                            }
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Modify")
                    }

                    // Delete button
                    FloatingActionButton(
                        onClick = {
                            selectedRowIndex.value?.let { rowIndex ->
                                viewModel.deleteDestination(context, rowIndex)
                                selectedRowIndex.value = null
                            }
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Delete")
                    }
                }

                // Create Destination Dialog
                if (showDialogCreate) {
                    AlertDialog(
                        onDismissRequest = { showDialogCreate = false },
                        title = { Text(text = "Create New Destination") },
                        text = {
                            Column {
                                Text("Name:")
                                Spacer(modifier = Modifier.height(8.dp))
                                TextField(
                                    value = createDestinationName,
                                    onValueChange = { createDestinationName = it },
                                    modifier = Modifier.fillMaxWidth()
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                Text("Description:")
                                Spacer(modifier = Modifier.height(8.dp))
                                TextField(
                                    value = createDestinationDescription,
                                    onValueChange = { createDestinationDescription = it },
                                    modifier = Modifier.fillMaxWidth()
                                )
                                // Additional fields for countryMode, type, picture, lastModify
                                // ...
                            }
                        },
                        confirmButton = {
                            Button(
                                onClick = {
                                    val newDestination = DestinationDomain(
                                        id = "${filterData.size + 1}",
                                        name = createDestinationName,
                                        description = createDestinationDescription,
                                        countryMode = "New Country Mode",
                                        type = "New Type",
                                        picture = "https://example.com/new_image.jpg",
                                        // Ensure lastModify receives a valid Long value
                                        lastModify = Timestamp(System.currentTimeMillis())
                                    )
                                    viewModel.createDestination(newDestination)
                                    showDialogCreate = false
                                }
                            ) {
                                Text("Create")
                            }
                        },
                        dismissButton = {
                            Button(
                                onClick = { showDialogCreate = false }
                            ) {
                                Text("Cancel")
                            }
                        }
                    )
                }


                // Modify Destination Dialog
                if (showDialogModify) {
                    AlertDialog(
                        onDismissRequest = { showDialogModify = false },
                        title = { Text(text = "Modify Destination") },
                        text = {
                            Column {
                                Text("Name:")
                                Spacer(modifier = Modifier.height(8.dp))
                                TextField(
                                    value = createDestinationName,
                                    onValueChange = { createDestinationName = it },
                                    modifier = Modifier.fillMaxWidth()
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                Text("Description:")
                                Spacer(modifier = Modifier.height(8.dp))
                                TextField(
                                    value = createDestinationDescription,
                                    onValueChange = { createDestinationDescription = it },
                                    modifier = Modifier.fillMaxWidth()
                                )
                                // Additional fields for countryMode, type, picture, lastModify
                                // ...
                            }
                        },
                        confirmButton = {
                            Button(
                                onClick = {
                                    selectedRowIndex.value?.let { rowIndex ->
                                        val updatedDestination = filterData[rowIndex]?.copy(
                                            name = createDestinationName,
                                            description = createDestinationDescription,
                                            // Ensure lastModify receives a valid Long value
                                            lastModify = Timestamp(System.currentTimeMillis())
                                        )
                                        if (updatedDestination != null) {
                                            viewModel.updateDestination(rowIndex, updatedDestination)
                                        }
                                    }
                                    showDialogModify = false
                                }
                            ) {
                                Text("Modify")
                            }
                        },
                        dismissButton = {
                            Button(
                                onClick = { showDialogModify = false }
                            ) {
                                Text("Cancel")
                            }
                        }
                    )
                }
            }
        }
    }
}

// Function to check connectivity (extracted for reusability)
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
