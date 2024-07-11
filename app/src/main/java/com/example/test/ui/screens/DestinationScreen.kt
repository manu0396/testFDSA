package com.example.test.ui.screens

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Title
                    H1Text(
                        text = context.getString(R.string.destination_title),
                        modifier = Modifier.padding(16.dp)
                        )
                    Spacer(modifier = Modifier.size(16.dp))

                    // Content (Table or Loading/Error)
                    if (showLoading) {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator()
                        }
                    } else {
                        if (data.isEmpty()) {
                            H2Text(
                                text = context.getString(R.string.no_data_found),
                                modifier = Modifier.wrapContentSize()
                            )
                        } else {
                            SimpleTable(data = data)
                        }
                    }
                    Spacer(modifier = Modifier.size(16.dp))

                    // Button Container
                    Row(
                        modifier = Modifier.fillMaxWidth(),
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
                            modifier = Modifier.sizeIn(minWidth = 60.dp, minHeight = 60.dp)
                        ) {
                            Text(text = context.getString(R.string.createDestiny))
                        }
                        FloatingActionButton(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = Color.White,
                            onClick = {
                                Toast.makeText(
                                    context, "Destiny modify", Toast.LENGTH_SHORT
                                ).show()
                            },
                            shape = RoundedCornerShape(10),
                            modifier = Modifier.sizeIn(minWidth = 60.dp, minHeight = 60.dp)
                        ) {
                            Text(text = context.getString(R.string.modifyDestiny))
                        }
                        FloatingActionButton(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = Color.White,
                            onClick = {
                                Toast.makeText(
                                    context, "Destiny deleted", Toast.LENGTH_SHORT
                                ).show()
                            },
                            shape = RoundedCornerShape(10),
                            modifier = Modifier.sizeIn(minWidth = 60.dp, minHeight = 60.dp)
                        ) {
                            Text(text = context.getString(R.string.removeDestiny))
                        }
                    }
                    Spacer(modifier = Modifier.weight(1f)) // Push VerticalDataSelector to bottom

                    // Vertical Data Selector
                    VerticalDataSelector(
                        data = data.mapNotNull { it?.id },
                        onItemSelected = {
                            Toast.makeText(
                                context, "Selected item: $it", Toast.LENGTH_SHORT
                            ).show()
                        }
                    )
                }
            }
        }
    }
}
