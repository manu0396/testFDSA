package com.example.test.ui.screens

import android.content.Context
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.test.R
import com.example.test.SharedViewModel
import com.example.test.ui.components.ItemComponent
import com.example.test.ui.components.SimpleAlertDialog
import com.example.test.ui.theme.TestTheme

@Composable
fun MainScreen(
    context:Context,
    viewModel:SharedViewModel
) {

    val data by viewModel.data.collectAsState()
    val showLoading by viewModel.showLoading.collectAsState()
    val showDialog by viewModel.showDialog.collectAsState()
    val messageDialog by viewModel.messageDialog.collectAsState()
    LaunchedEffect(key1 = data){
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
        Surface(modifier = Modifier.fillMaxSize()) {
            Box(contentAlignment = Alignment.TopCenter) {
                Text(text = context.getString(R.string.titleMainScreen))
            }
            Box(contentAlignment = Alignment.Center) {
                if(data.isEmpty()){
                    Text(text = context.getString(R.string.usersNotFound))
                }else{
                    LazyColumn{
                        items(data.size){index->
                            data[index]?.let {
                                ItemComponent(item = it)
                            }
                        }
                    }
                }
            }
        }
    }
}