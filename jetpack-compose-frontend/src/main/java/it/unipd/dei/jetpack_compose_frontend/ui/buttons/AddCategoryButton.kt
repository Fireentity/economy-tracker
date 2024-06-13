package it.unipd.dei.jetpack_compose_frontend.ui.buttons

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import it.unipd.dei.common_backend.models.Category
import it.unipd.dei.common_backend.utils.DisplayToast
import it.unipd.dei.common_backend.viewModels.CategoryViewModel
import it.unipd.dei.jetpack_compose_frontend.R

@Composable
fun AddCategoryButton(
    category: Category,
    onSuccess: () -> Unit,
    onFailure: () -> Unit,
    categoryViewMode: CategoryViewModel
) {
    val context = LocalContext.current
    val categorySuccessfullyAdded = stringResource(R.string.category_added_successfully)
    val categoryCreationFailed = stringResource(R.string.category_creation_failed)
    Button(onClick = { categoryViewMode.upsertCategory(
        Category(
            category.uuid,
            category.identifier,
            category.createdAt,
            category.updatedAt
        ),
        {
            DisplayToast.displayGeneric(context, categorySuccessfullyAdded)
            onSuccess()
        },
        {
            DisplayToast.displayGeneric(context, categoryCreationFailed)
            onFailure()
        }
    ) }) {
        Text(text = "Confirm")
    }
}