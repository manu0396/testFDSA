package com.example.test.ui.screens

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.test.R
import com.example.test.SharedViewModel
import com.example.test.ui.components.AppBar
import com.example.test.ui.components.SimpleAlertDialog
import com.example.test.ui.components.SimpleTable
import com.example.test.ui.components.VerticalDataSelector
import com.example.test.ui.theme.TestTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DestinationScreen(
    context: Context,
    viewModel: SharedViewModel,
    navController: NavController,
) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp

    val data by viewModel.data.collectAsState()
    val showLoading by viewModel.showLoading.collectAsState()
    val showDialog by viewModel.showDialog.collectAsState()
    val messageDialog by viewModel.messageDialog.collectAsState()
    LaunchedEffect(key1 = data) {
        viewModel.getResults(context)
    }

    TestTheme {
        if (showLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize() // it will fill parent box
                    .padding(8.dp)
            ) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
        }
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
            contentWindowInsets = WindowInsets(32.dp)
        ) { paddingValues ->
            Surface(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
            ) {
                Box(contentAlignment = Alignment.TopCenter) {
                    Text(text = context.getString(R.string.destination_title))
                    Spacer(modifier = Modifier.size(16.dp))
                    Row(modifier = Modifier.fillMaxWidth()) {
                        FloatingActionButton(
                            onClick = {
                                Toast.makeText(
                                    context,
                                    "Destiny created",
                                    Toast.LENGTH_SHORT
                                ).show()
                            },
                            shape = RoundedCornerShape(10),
                            modifier = Modifier.sizeIn(minWidth = 50.dp, minHeight = 50.dp)
                        ) {
                            Text(text = context.getString(R.string.createDestiny))
                        }
                        FloatingActionButton(
                            onClick = {
                                Toast.makeText(
                                    context,
                                    "Destiny modify",
                                    Toast.LENGTH_SHORT
                                ).show()
                            },
                            shape = RoundedCornerShape(10),
                            modifier = Modifier.sizeIn(minWidth = 50.dp, minHeight = 50.dp)
                        ) {
                            Text(text = context.getString(R.string.createDestiny))
                        }
                        FloatingActionButton(
                            onClick = {
                                Toast.makeText(
                                    context,
                                    "Destiny deleted",
                                    Toast.LENGTH_SHORT
                                ).show()
                            },
                            shape = RoundedCornerShape(10),
                            modifier = Modifier.sizeIn(minWidth = 50.dp, minHeight = 50.dp)
                        ) {
                            Text(text = context.getString(R.string.createDestiny))
                        }
                    }
                }
                Spacer(modifier = Modifier.size(16.dp))
                Row(
                    modifier = Modifier
                        .wrapContentSize(Alignment.BottomCenter)
                        .padding(8.dp)
                ) {
                    VerticalDataSelector(
                        data = data.mapNotNull { it?.id },
                        onItemSelected = {
                            Toast.makeText(
                                context,
                                "Selected item: $it",
                                Toast.LENGTH_SHORT
                            ).show()
                        })
                    Spacer(modifier = Modifier.size(16.dp))
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(width = screenWidth * (2 / 3), height = screenHeight * (2 / 3))
                            .padding(16.dp)
                    ) {
                        SimpleTable(data = if (data.isEmpty() || data.first() == null) listOf() else data)
                    }
                }

            }
        }
    }
}