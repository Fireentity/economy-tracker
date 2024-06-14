package it.unipd.dei.jetpack_compose_frontend.ui.buttons

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import it.unipd.dei.common_backend.models.Category
import it.unipd.dei.common_backend.viewModels.CategoryViewModel
import it.unipd.dei.common_backend.viewModels.MovementWithCategoryViewModel
import it.unipd.dei.common_backend.viewModels.SummaryViewModel
import it.unipd.dei.jetpack_compose_frontend.R
import it.unipd.dei.jetpack_compose_frontend.ui.dialog.DeleteCategoryDialog

@Composable
fun ShowDeleteCategoryDialogButton(
    categoryViewModel: CategoryViewModel,
    movementWithCategoryViewModel: MovementWithCategoryViewModel,
    category: Category,
    summaryViewModel: SummaryViewModel
) {
    var showDeleteCategoryDialog by remember { mutableStateOf(false) }

    TextButton(
        modifier = Modifier.height(48.dp),
        onClick = {
            showDeleteCategoryDialog = true
        }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Icon(
                imageVector = Icons.Outlined.Delete,
                contentDescription = stringResource(id = R.string.delete_category)
            )
            Text(
                text = stringResource(id = R.string.delete_category),
                modifier = Modifier.padding(start = 12.dp)
            )
        }
    }

    if (showDeleteCategoryDialog) {
        DeleteCategoryDialog(
            category,
            categoryViewModel,
            movementWithCategoryViewModel,
            summaryViewModel,
            {
                showDeleteCategoryDialog = false
            }
        ) {
            showDeleteCategoryDialog = false
        }
    }
}