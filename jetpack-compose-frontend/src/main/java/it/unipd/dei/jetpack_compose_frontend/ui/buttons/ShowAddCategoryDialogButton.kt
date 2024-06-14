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
import androidx.compose.ui.res.stringResource
import it.unipd.dei.common_backend.viewModels.CategoryViewModel
import it.unipd.dei.common_backend.viewModels.MovementWithCategoryViewModel
import it.unipd.dei.jetpack_compose_frontend.R
import it.unipd.dei.jetpack_compose_frontend.ui.dialog.UpsertCategoryDialog

@Composable
fun ShowAddCategoryDialogButton(
    categoryViewModel: CategoryViewModel,
    movementWithCategoryViewModel: MovementWithCategoryViewModel
) {
    var showDialog by remember { mutableStateOf(false) }
    FloatingActionButton(onClick = {
        showDialog = true
    }) {
        Icon(imageVector = Icons.Filled.Add, contentDescription = stringResource(id = R.string.create_category))
    }
    if (showDialog) {
        UpsertCategoryDialog(
            categoryViewModel,
            movementWithCategoryViewModel,
            stringResource(id = R.string.create_category),
            { showDialog = false })
    }
}