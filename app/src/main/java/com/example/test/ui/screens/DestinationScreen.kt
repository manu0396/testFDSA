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
import com.example.test.ui.components.AppBar
import com.example.test.ui.components.H1Text
import com.example.test.ui.components.H2Text
import com.example.test.ui.components.SimpleAlertDialog
import com.example.test.ui.components.SimpleTable
import com.example.test.ui.components.VerticalDataSelector
import com.example.test.ui.theme.TestTheme

@Composable
fun DestinationScreen(
    context: Context,
    viewModel: SharedViewModel,
    navController: NavController,
) {
    val data by viewModel.data.collectAsState()
    val showLoading by viewModel.showLoading.collectAsState()
    val showDialog by viewModel.showDialog.collectAsState()
    val messageDialog by viewModel.messageDialog.collectAsState()

    LaunchedEffect(key1 = data) {
        viewModel.getResults(context)
    }

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
                        if (data.isEmpty()) {
                            H2Text(
                                text = context.getString(R.string.no_data_found),
                                modifier = Modifier.align(Alignment.Center)
                            )
                        } else {
                            SimpleTable(
                                data = data,
                                modifier = Modifier.align(Alignment.Center)
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
