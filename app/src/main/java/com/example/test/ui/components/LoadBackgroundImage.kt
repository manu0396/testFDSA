package com.example.test.ui.components

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.example.test.R

@Composable
fun LoadBackgroundImage(
    modifier: Modifier = Modifier
) {
    val imagePainter = painterResource(id = R.drawable.background)
    Log.d("MainActivity", "Loading background image: $imagePainter")
    Box(
        modifier = modifier
    ) {
        Image(
            painter = imagePainter,
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
    }
}
