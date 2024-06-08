package it.unipd.dei.jetpack_compose_frontend.ui.buttons

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import it.unipd.dei.jetpack_compose_frontend.ui.dialog.AddCategoryDialog

@Composable
fun ShowAddCategoryDialogButton() {
    val showDialog = remember { mutableStateOf(false) }
    FloatingActionButton(onClick = {
        showDialog.value = true
    }) {
        Icon(imageVector = Icons.Filled.Add, contentDescription = "")
    }
    if(showDialog.value) {
        AddCategoryDialog()
    }
}