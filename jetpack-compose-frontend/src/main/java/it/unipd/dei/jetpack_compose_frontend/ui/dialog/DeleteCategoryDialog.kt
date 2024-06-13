package it.unipd.dei.jetpack_compose_frontend.ui.dialog

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import it.unipd.dei.common_backend.models.Category
import it.unipd.dei.common_backend.utils.DisplayToast
import it.unipd.dei.common_backend.viewModels.CategoryViewModel
import it.unipd.dei.jetpack_compose_frontend.R

@Composable
fun DeleteCategoryDialog(
    category: Category,
    categoryViewModel: CategoryViewModel,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    val context = LocalContext.current
    val categorySuccessfullyRemoved = stringResource(R.string.category_deleted_successfully)
    val categoryCreationFailed = stringResource(R.string.category_deletion_failed)
    AlertDialog(
        onDismissRequest = { },
        title = { Text(text = stringResource(id = R.string.delete_category)) },
        text = { Text(text = stringResource(id = R.string.category_deletion_confirmation)) },
        confirmButton = {
            TextButton(onClick = {
                categoryViewModel.deleteCategory(
                    category,
                    {
                        DisplayToast.displayGeneric(context, categorySuccessfullyRemoved)
                        onConfirm()
                    },
                    {
                        DisplayToast.displayGeneric(context, categoryCreationFailed)
                        onDismiss()
                    },
                )
            }) {
                Text(text = stringResource(R.string.delete))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = "Cancel")
            }
        }
    )
}