package com.fhanafi.cerdikia.ui.components

import android.icu.text.CaseMap.Title
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SmallTopAppBar
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTopBar(title: String? = null, onBackClick: (() -> Unit)? = null) {
    TopAppBar(
        title = {
            if(!title.isNullOrEmpty()){
                Text(title)
            }
        },
        modifier = Modifier.fillMaxWidth(),
        navigationIcon = {
            if(onBackClick != null){
                IconButton(onClick = onBackClick) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                }
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun CustomTopBarPreview() {
    CustomTopBar("Details")
}