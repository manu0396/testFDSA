package com.example.test.ui.screens

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.test.R
import com.example.test.SharedViewModel
import com.example.test.data.models.Timestamp
import com.example.test.domain.models.DestinationDomain
import com.example.test.ui.components.*
import com.example.test.ui.theme.TestTheme
import com.example.test.utils.DateUtils
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
    var showDialogDelete by remember { mutableStateOf(false) }
    var createDestinationName by remember { mutableStateOf("") }
    var createDestinationDescription by remember { mutableStateOf("") }
    var createDestinationCountryMode by remember { mutableStateOf("") }
    var createDestinationType by remember { mutableStateOf("") }
    var createDestinationPicture by remember { mutableStateOf("") }
    var createDestinationLastModify by remember { mutableLongStateOf(DateUtils.extractMilliseconds(System.currentTimeMillis().toString())) }
    // State for managing modify mode
    var isModifyMode by remember { mutableStateOf(false) }

    // Launched effect to trigger initial data fetching
    LaunchedEffect(key1 = data) {
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
                                onCellDeleted = { rowIndex, _ ->
                                    viewModel.deleteDestination(rowIndex)
                                },
                                onCellSelected = { rowIndex ->
                                    selectedRowIndex.value = rowIndex
                                },
                                selectedRowIndex = selectedRowIndex,
                                isModifyMode = isModifyMode
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
                                createDestinationCountryMode = selectedDestination?.countryMode ?: ""
                                createDestinationType = selectedDestination?.type ?: ""
                                createDestinationPicture = selectedDestination?.picture ?: ""
                                createDestinationLastModify = selectedDestination?.lastModify?.millis ?: 0L
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
                                showDialogDelete = true
                                selectedRowIndex.value = rowIndex
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
                                Spacer(modifier = Modifier.height(16.dp))
                                Text("Country Mode:")
                                Spacer(modifier = Modifier.height(8.dp))
                                TextField(
                                    value = createDestinationCountryMode,
                                    onValueChange = { createDestinationCountryMode = it },
                                    modifier = Modifier.fillMaxWidth()
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                Text("Type:")
                                Spacer(modifier = Modifier.height(8.dp))
                                TextField(
                                    value = createDestinationType,
                                    onValueChange = { createDestinationType = it },
                                    modifier = Modifier.fillMaxWidth()
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                Text("Picture:")
                                Spacer(modifier = Modifier.height(8.dp))
                                TextField(
                                    value = createDestinationPicture,
                                    onValueChange = { createDestinationPicture = it },
                                    modifier = Modifier.fillMaxWidth()
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                Text("Last Modify:")
                                Spacer(modifier = Modifier.height(8.dp))
                                TextField(
                                    value = createDestinationLastModify.toString(),
                                    onValueChange = { it: String ->
                                        createDestinationLastModify = it.toLongOrNull() ?: System.currentTimeMillis()
                                                    },
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        },
                        confirmButton = {
                            Button(
                                onClick = {
                                    val newDestination = DestinationDomain(
                                        id = "${filterData.size + 1}",
                                        name = createDestinationName,
                                        description = createDestinationDescription,
                                        countryMode = createDestinationCountryMode,
                                        type = createDestinationType,
                                        picture = createDestinationPicture,
                                        lastModify = Timestamp(createDestinationLastModify.toLong())
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

                // Confirm Delete Destination Dialog
                if (showDialogDelete) {
                    AlertDialog(
                        onDismissRequest = { showDialogDelete = false },
                        title = { Text(text = "Confirm Delete") },
                        text = { Text("Are you sure you want to delete this destination?") },
                        confirmButton = {
                            Button(
                                onClick = {
                                    selectedRowIndex.value.takeIf { it != -1 }?.let {
                                        viewModel.deleteDestination(it)
                                        selectedRowIndex.value = null // Clear selection after deletion
                                    }
                                    showDialogDelete = false
                                }
                            ) {
                                Text("Delete")
                            }
                        },
                        dismissButton = {
                            Button(
                                onClick = { showDialogDelete = false }
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
                                Spacer(modifier = Modifier.height(16.dp))
                                Text("Country Mode:")
                                Spacer(modifier = Modifier.height(8.dp))
                                TextField(
                                    value = createDestinationCountryMode,
                                    onValueChange = { createDestinationCountryMode = it },
                                    modifier = Modifier.fillMaxWidth()
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                Text("Type:")
                                Spacer(modifier = Modifier.height(8.dp))
                                TextField(
                                    value = createDestinationType,
                                    onValueChange = { createDestinationType = it },
                                    modifier = Modifier.fillMaxWidth()
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                Text("Picture:")
                                Spacer(modifier = Modifier.height(8.dp))
                                TextField(
                                    value = createDestinationPicture,
                                    onValueChange = { createDestinationPicture = it },
                                    modifier = Modifier.fillMaxWidth()
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                Text("Last Modify:")
                                Spacer(modifier = Modifier.height(8.dp))
                                TextField(
                                    value = createDestinationLastModify.toString(),
                                    onValueChange = { createDestinationLastModify = it.toLongOrNull() ?: System.currentTimeMillis() },
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        },
                        confirmButton = {
                            Button(
                                onClick = {
                                    selectedRowIndex.value?.let { rowIndex ->
                                        val updatedDestination = filterData[rowIndex]?.copy(
                                            name = createDestinationName,
                                            description = createDestinationDescription,
                                            countryMode = createDestinationCountryMode,
                                            type = createDestinationType,
                                            picture = createDestinationPicture,
                                            lastModify = Timestamp(createDestinationLastModify)
                                        )
                                        updatedDestination?.let {
                                            viewModel.updateDestination(rowIndex, it)
                                        }
                                        showDialogModify = false
                                    }
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
