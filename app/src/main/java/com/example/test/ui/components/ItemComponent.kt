package com.example.test.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import coil.memory.MemoryCache
import coil.request.ImageRequest
import com.example.test.domain.models.DomainModel
import com.example.test.ui.theme.TestTheme
import com.skydoves.landscapist.coil.CoilImage

@Composable
fun ItemComponent(
    item:DomainModel
) {
    TestTheme {
        Card() {
            Row(horizontalArrangement =Arrangement.SpaceBetween) {
                CoilImage(
                    imageRequest = ImageRequest.Builder(LocalContext.current)
                        .data(item.picture)
                        .build(),
                    imageLoader = {
                        ImageLoader.Builder(LocalContext.current)
                            .memoryCache(MemoryCache.Builder(LocalContext.current).maxSizePercent(0.25).build())
                            .crossfade(true)
                            .build()
                    },
                    contentDescription = item.name,
                    modifier = Modifier
                        .size(120.dp),

                    loading = {
                        CircularProgressIndicator()
                    },
                    // shows an error text message when request failed.
                    failure = {
                        Text(text = "image request failed.")
                    }
                )
                Column(verticalArrangement = Arrangement.Bottom) {
                    Text(text = item.email)
                    Spacer(modifier = Modifier.size(2.dp))
                    Text(text = item.name)
                    Spacer(modifier = Modifier.size(2.dp))
                    Text(text = item.location)
                }
            }

        }
    }
}