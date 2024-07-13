package com.example.test.ui.screens

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
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
import com.example.test.domain.models.DestinationDomain
import com.example.test.ui.components.*
import com.example.test.ui.theme.CustomTheme
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

    val filterData by remember(data, localData) {
        derivedStateOf {
            if (NetworkUtils.checkConnectivity(context)) {
                data
            } else {
                localData
            }
        }
    }

    // State for managing selected row index
    var selectedRowIndex by remember { mutableStateOf<Int?>(viewModel.selectedRowIndex.value) }
    LaunchedEffect(key1 = viewModel.selectedRowIndex) {
        viewModel.selectedRowIndex.collect { index ->
            selectedRowIndex = index
        }
    }

    // State for managing create and modify dialogs
    var showDialogCreate by remember { mutableStateOf(false) }
    var showDialogModify by remember { mutableStateOf(false) }
    var showDialogDelete by remember { mutableStateOf(false) }
    var createDestinationId by remember { mutableStateOf("") }
    var createDestinationName by remember { mutableStateOf("") }
    var createDestinationDescription by remember { mutableStateOf("") }
    var createDestinationCountryMode by remember { mutableStateOf("") }
    var createDestinationType by remember { mutableStateOf("") }
    var createDestinationPicture by remember { mutableStateOf("") }
    var createDestinationLastModify by remember {
        mutableStateOf(
            Timestamp(
                DateUtils.extractMilliseconds(
                    System.currentTimeMillis().toString()
                )
            )
        )
    }
    // State for managing modify mode
    var isModifyMode by remember { mutableStateOf(false) }

    // State for managing search result dialog
    var showDialogSearchResult by remember { mutableStateOf(false) }
    var searchResultItem by remember { mutableStateOf<DestinationDomain?>(null) }

    // Launched effect to trigger initial data fetching
    LaunchedEffect(key1 = filterData) {
        viewModel.getResults(context)
        NetworkUtils.checkConnectivity(context)
    }

    // Scaffold and UI components
    CustomTheme {
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
                        .padding(top = 8.dp, bottom = 16.dp)
                )
                Spacer(modifier = Modifier.height(32.dp))
                // Search and Filter Column
                Column(
                    verticalArrangement = Arrangement.Top,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp)
                ) {
                    // VerticalDataSelector with Search Button
                    var selectedItem by remember { mutableStateOf("") }

                    VerticalDataSelector(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 0.dp, vertical = 8.dp),
                        data = filterData.map { it?.name ?: "" },
                        onItemSelected = { selected ->
                            selectedItem = selected
                        },
                        onRowSelected = { index, resultSearch ->
                            selectedRowIndex = index
                            isModifyMode = false
                            resultSearch?.let {
                                searchResultItem = filterData[index]
                                showDialogSearchResult = true
                            }
                        },
                        viewModel = viewModel,
                        context = context
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Display EditableTable with data
                    EditableTable(
                        data = filterData,
                        modifier = Modifier
                            .padding(horizontal = 8.dp)
                            .align(Alignment.CenterHorizontally)
                            .background(
                                color = Color(0xFFEAFAF5),
                                shape = RoundedCornerShape(8.dp)
                            )
                            .padding(16.dp),
                        onCellEdited = { rowIndex, colIndex, newValue ->
                            viewModel.data.value.getOrNull(rowIndex)?.let { destination ->
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
                            selectedRowIndex = rowIndex
                        },
                        selectedRowIndex = remember { mutableStateOf(selectedRowIndex) },
                        isModifyMode = isModifyMode
                    )

                    // Search button inside VerticalDataSelector
                    Button(
                        onClick = {
                            val index = filterData.indexOfFirst { it?.name == selectedItem }
                            if (index >= 0) {
                                selectedRowIndex = index
                                searchResultItem = filterData[index]
                                showDialogSearchResult = true
                            } else {
                                searchResultItem = null
                                showDialogSearchResult = true
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
                        }
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
                        shape = RoundedCornerShape(50),
                        onClick = {
                            showDialogCreate = true
                        },
                        modifier = Modifier
                            .padding(horizontal = 8.dp)
                            .weight(1f),
                        containerColor = Color(0xFFD4EFE7),
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    ) {
                        Text("Create")
                    }

                    // Modify button
                    FloatingActionButton(
                        shape = RoundedCornerShape(50),
                        onClick = {
                            selectedRowIndex?.let { rowIndex ->
                                showDialogModify = true
                                val selectedDestination = filterData[rowIndex]
                                createDestinationName = selectedDestination?.name ?: ""
                                createDestinationDescription =
                                    selectedDestination?.description ?: ""
                                createDestinationCountryMode =
                                    selectedDestination?.countryMode ?: ""
                                createDestinationType = selectedDestination?.type ?: ""
                                createDestinationPicture = selectedDestination?.picture ?: ""
                                createDestinationLastModify =
                                    selectedDestination?.lastModify ?: Timestamp(0)
                            } ?: viewModel.showDialog(context.getString(R.string.error_modify))
                        },
                        modifier = Modifier
                            .padding(horizontal = 8.dp)
                            .weight(1f),
                        containerColor = Color(0xFFD4EFE7),
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    ) {
                        Text("Modify")
                    }

                    // Delete button
                    FloatingActionButton(
                        shape = RoundedCornerShape(50),
                        onClick = {
                            selectedRowIndex?.let { rowIndex ->
                                showDialogDelete = true
                            }
                        },
                        modifier = Modifier
                            .padding(horizontal = 8.dp)
                            .weight(1f),
                        containerColor = Color(0xFFD4EFE7),
                        contentColor = MaterialTheme.colorScheme.onPrimary
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
                                Text("ID:")
                                Spacer(modifier = Modifier.size(8.dp))
                                TextField(
                                    value = createDestinationId,
                                    onValueChange = { createDestinationId = it },
                                    modifier = Modifier.fillMaxWidth()
                                )
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
                                BasicTextField(
                                    value = createDestinationLastModify.toString(),
                                    onValueChange = {
                                        createDestinationLastModify = Timestamp(it.toLong())
                                    },
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        },
                        confirmButton = {
                            Button(
                                onClick = {
                                    viewModel.createDestination(
                                        DestinationDomain(
                                            id = createDestinationId,
                                            name = createDestinationName,
                                            description = createDestinationDescription,
                                            countryMode = createDestinationCountryMode,
                                            type = createDestinationType,
                                            picture = createDestinationPicture,
                                            lastModify = createDestinationLastModify

                                        )
                                    )
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
                                BasicTextField(
                                    value = createDestinationLastModify.toString(),
                                    onValueChange = {
                                        createDestinationLastModify = Timestamp(it.toLong())
                                    },
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        },
                        confirmButton = {
                            Button(
                                onClick = {
                                    selectedRowIndex?.let { rowIndex ->
                                        viewModel.updateDestination(
                                            rowIndex,
                                            DestinationDomain(
                                                id = createDestinationId,
                                                createDestinationName,
                                                createDestinationDescription,
                                                createDestinationCountryMode,
                                                createDestinationType,
                                                createDestinationPicture,
                                                Timestamp(createDestinationLastModify.millis)
                                            )
                                        )
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

                // Delete Destination Dialog
                if (showDialogDelete) {
                    AlertDialog(
                        onDismissRequest = { showDialogDelete = false },
                        title = { Text(text = "Delete Destination") },
                        text = {
                            Text("Are you sure you want to delete this destination?")
                        },
                        confirmButton = {
                            Button(
                                onClick = {
                                    selectedRowIndex?.let { rowIndex ->
                                        viewModel.deleteDestination(rowIndex)
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

                // Search Result Dialog
                if (showDialogSearchResult) {
                    AlertDialog(
                        onDismissRequest = { showDialogSearchResult = false },
                        title = { Text(text = "Search Result") },
                        text = {
                            searchResultItem?.let { item ->
                                Column {
                                    Text("Name: ${item.name}")
                                    Text("Description: ${item.description}")
                                    Text("Country Mode: ${item.countryMode}")
                                    Text("Type: ${item.type}")
                                    Text("Picture: ${item.picture}")
                                    Text("Last Modify: ${item.lastModify?.millis}")
                                }
                            } ?: Text("Item not found")
                        },
                        confirmButton = {
                            Button(
                                onClick = { showDialogSearchResult = false }
                            ) {
                                Text("OK")
                            }
                        }
                    )
                }
            }
        }
    }
}
