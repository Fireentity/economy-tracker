package it.unipd.dei.jetpack_compose_frontend.ui.buttons

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import it.unipd.dei.common_backend.models.Category
import it.unipd.dei.common_backend.utils.DisplayToast
import it.unipd.dei.common_backend.view.CategoryViewModel
import it.unipd.dei.jetpack_compose_frontend.R
import java.util.UUID

@Composable
fun AddCategoryButton(
    categoryIdentifier: String,
    categoryViewMode: CategoryViewModel = viewModel(),
) {
    val context = LocalContext.current
    val categorySuccessfullyAdded = stringResource(R.string.category_added_successfully)
    val categoryCreationFailed = stringResource(R.string.category_creation_failed)
    Button(onClick = { categoryViewMode.upsertCategory(
        Category(
            UUID.randomUUID(),
            categoryIdentifier,
            System.currentTimeMillis(),
            System.currentTimeMillis()
        ),
        {
            DisplayToast.displayGeneric(context, categorySuccessfullyAdded)
        },
        {
            DisplayToast.displayGeneric(context, categoryCreationFailed)
        }
    ) }) {
        Text(text = "Confirm")
    }
}