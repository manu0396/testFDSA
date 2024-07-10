package com.example.test.ui.components

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.sp
import com.example.test.R


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar(
    title: String = "",
    centered: Boolean = false,
    actions: @Composable RowScope.() -> Unit = {},
    onBackClick: (() -> Unit)? = null,
) {
    val colors = TopAppBarDefaults.smallTopAppBarColors(
        containerColor = MaterialTheme.colorScheme.primary,
        titleContentColor = MaterialTheme.colorScheme.onPrimary,
        actionIconContentColor = MaterialTheme.colorScheme.onPrimary,
        navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
    )
    val textUi = @Composable {
        Text(title, fontSize = 24.sp)
    }
    if (centered) {
        CenterAlignedTopAppBar(
            title = textUi,
            navigationIcon = {
                AppBarBackButton(onBackClick)
            },
            actions = actions,
            colors = colors,
        )
    } else {
        TopAppBar(
            title = textUi,
            navigationIcon = {
                AppBarBackButton(onBackClick)
            },
            actions = actions,
            colors = colors,
        )
    }
}

@Composable
fun AppBarBackButton(
    onClick: (() -> Unit)? = null,
) {
    if (onClick == null) return
    IconButton(onClick = { onClick.invoke() }) {
        Icon(
            painter = painterResource(id = R.drawable.baseline_arrow_back_24),
            contentDescription = "icon_back"
        )
    }
}

@Composable
fun AppBarActionButton(
    iconResId: Int,
    iconSize: Dp? = null,
    contentDescription: String? = null,
    onClick: (() -> Unit)? = null,
) {
    if (onClick == null) return
    IconButton(onClick = { onClick.invoke() }) {
        Icon(
            painter = painterResource(id = iconResId),
            contentDescription = contentDescription,
            modifier = if (iconSize == null) Modifier else Modifier.size(iconSize)
        )
    }
}