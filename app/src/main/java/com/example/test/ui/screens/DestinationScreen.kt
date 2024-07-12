package com.example.test.ui.screens

import android.content.Context
import android.util.Log
import android.widget.Toast
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
import com.example.test.utils.DateUtils.formatDateFromMillis
import com.example.test.utils.NetworkUtils
import com.example.test.utils.NetworkUtils.checkConnectivity

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
    val filterData by remember(data, localData) {
        derivedStateOf {
            if (checkConnectivity(context)) {
                data
            } else {
                localData
            }
        }
    }

    // State for managing selected row index
    var selectedRowIndex = remember { mutableStateOf(viewModel.selectedRowIndex.value) }
    LaunchedEffect(key1 = viewModel.selectedRowIndex) {
        viewModel.selectedRowIndex.collect { index ->
            selectedRowIndex.value = index
        }
    }
    // State for managing create and modify dialogs
    var showDialogCreate by remember { mutableStateOf(false) }
    var showDialogModify by remember { mutableStateOf(false) }
    var showDialogDelete by remember { mutableStateOf(false) }
    var createDestinationName by remember { mutableStateOf("") }
    var createDestinationDescription by remember { mutableStateOf("") }
    var createDestinationCountryMode by remember { mutableStateOf("") }
    var createDestinationType by remember { mutableStateOf("") }
    var createDestinationPicture by remember { mutableStateOf("") }
    var createDestinationLastModify by remember {
        mutableLongStateOf(
            DateUtils.extractMilliseconds(
                System.currentTimeMillis().toString()
            )
        )
    }
    // State for managing modify mode
    var isModifyMode by remember { mutableStateOf(false) }

    // Launched effect to trigger initial data fetching
    LaunchedEffect(key1 = filterData) {
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
                // Search and Filter Column
                Column(
                    verticalArrangement = Arrangement.Top,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp)
                ) {
                    // VerticalDataSelector
                    var selectedItem by remember { mutableStateOf("") }

                    VerticalDataSelector(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        data = filterData.map { it?.name ?: "" },
                        onItemSelected = { selected ->
                            selectedItem = selected
                        },
                        viewModel = viewModel
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            val index = filterData.indexOfFirst { it?.name == selectedItem }
                            if (index >= 0) {
                                selectedRowIndex = mutableStateOf(index)
                            } else {
                                Toast.makeText(context, "Item not found", Toast.LENGTH_SHORT).show()
                            }
                        },
                        modifier = Modifier.align(Alignment.End)
                    ) {
                        Text("Search")
                    }
                }
                // Loading or Error message when data is loading or empty
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 120.dp, bottom = 120.dp),
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
                            val selectedRowIndexState =
                                remember { mutableStateOf(selectedRowIndex) }
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
                                            6 -> {
                                                val timestampValue = newValue.toLongOrNull() ?: 0L
                                                destination.copy(
                                                    lastModify = Timestamp(
                                                        timestampValue
                                                    )
                                                )
                                            }

                                            else -> destination
                                        }
                                        viewModel.updateDestination(rowIndex, updatedDestination)
                                    }
                                },
                                onCellDeleted = { rowIndex, _ ->
                                    viewModel.deleteDestination(rowIndex)
                                },
                                onCellSelected = { rowIndex ->
                                    selectedRowIndexState.value = mutableStateOf(rowIndex)
                                },
                                selectedRowIndex = selectedRowIndexState.value,
                                isModifyMode = isModifyMode
                            )

                        }
                    }

                    // Row of action buttons (Create, Modify, Delete)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp, vertical = 8.dp)
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
                                selectedRowIndex?.let { rowIndex ->
                                    Log.d("Row", "modifyrow row: $rowIndex")
                                    showDialogModify = true
                                    val selectedDestination = filterData[rowIndex.value ?: 0]
                                    createDestinationName = selectedDestination?.name ?: ""
                                    createDestinationDescription =
                                        selectedDestination?.description ?: ""
                                    createDestinationCountryMode =
                                        selectedDestination?.countryMode ?: ""
                                    createDestinationType = selectedDestination?.type ?: ""
                                    createDestinationPicture = selectedDestination?.picture ?: ""
                                    createDestinationLastModify =
                                        selectedDestination?.lastModify?.millis ?: 0L
                                    if (selectedDestination != null) {
                                        rowIndex.value?.let{index ->
                                            viewModel.updateDestination(
                                                index,
                                                selectedDestination
                                            )
                                        }
                                    } else {
                                        viewModel.showDialog(context.getString(R.string.error_modify))
                                    }
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
                                    Log.d("Row", "delete row: $rowIndex")
                                    showDialogDelete = true
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
                                        value = if (createDestinationLastModify == 0L) formatDateFromMillis(
                                            System.currentTimeMillis()
                                        ) else formatDateFromMillis(createDestinationLastModify),
                                        onValueChange = { it: String ->
                                            createDestinationLastModify =
                                                it.toLongOrNull() ?: System.currentTimeMillis()
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
                                            selectedRowIndex.value =
                                                null // Clear selection after deletion
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
                                        onValueChange = {
                                            createDestinationLastModify =
                                                it.toLongOrNull() ?: System.currentTimeMillis()
                                        },
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                }
                            },
                            confirmButton = {
                                Button(
                                    onClick = {
                                        selectedRowIndex?.let { rowIndex ->
                                            val updatedDestination =
                                                filterData[rowIndex.value ?: 0]?.copy(
                                                    name = createDestinationName,
                                                    description = createDestinationDescription,
                                                    countryMode = createDestinationCountryMode,
                                                    type = createDestinationType,
                                                    picture = createDestinationPicture,
                                                    lastModify = Timestamp(
                                                        createDestinationLastModify
                                                    )
                                                )
                                            updatedDestination?.let {
                                                viewModel.updateDestination(rowIndex.value ?: 0, it)
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
}
