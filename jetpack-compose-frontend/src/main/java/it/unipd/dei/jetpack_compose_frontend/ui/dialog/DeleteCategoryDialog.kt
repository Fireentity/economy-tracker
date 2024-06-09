package it.unipd.dei.jetpack_compose_frontend.ui.dialog

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import it.unipd.dei.common_backend.models.Category
import it.unipd.dei.common_backend.view.CategoryViewModel

@Composable
fun DeleteCategoryDialog(
    category: Category,
    categoryViewModel: CategoryViewModel,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        onDismissRequest = { },
        title = { Text(text = "Delete Category") },
        text = { Text(text = "Are you sure you want to delete this category?") },
        confirmButton = {
            TextButton(onClick = {
                categoryViewModel.deleteCategory(category, onConfirm) { onDismiss() }
            }) {
                Text(text = "Delete")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = "Cancel")
            }
        }
    )
}