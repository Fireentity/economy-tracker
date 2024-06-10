package it.unipd.dei.jetpack_compose_frontend.ui.buttons

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import it.unipd.dei.common_backend.viewModels.CategoryViewModel
import it.unipd.dei.jetpack_compose_frontend.ui.dialog.UpsertCategoryDialog

@Composable
fun ShowAddCategoryDialogButton(categoryViewModel: CategoryViewModel) {
    var showDialog by remember { mutableStateOf(false) }
    FloatingActionButton(onClick = {
        showDialog = true
    }) {
        Icon(imageVector = Icons.Filled.Add, contentDescription = "")
    }
    if (showDialog) {
        UpsertCategoryDialog(categoryViewModel, { showDialog = false })
    }
}