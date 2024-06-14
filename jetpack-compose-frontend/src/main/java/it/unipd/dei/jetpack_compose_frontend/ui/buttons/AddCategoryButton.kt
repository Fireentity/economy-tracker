package it.unipd.dei.jetpack_compose_frontend.ui.buttons

import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import it.unipd.dei.common_backend.models.Category
import it.unipd.dei.common_backend.utils.DisplayToast
import it.unipd.dei.common_backend.viewModels.CategoryViewModel
import it.unipd.dei.common_backend.viewModels.MovementWithCategoryViewModel
import it.unipd.dei.jetpack_compose_frontend.R

@Composable
fun AddCategoryButton(
    category: Category,
    onSuccess: () -> Unit,
    onFailure: () -> Unit,
    categoryViewModel: CategoryViewModel,
    movementWithCategoryViewModel: MovementWithCategoryViewModel
) {
    val context = LocalContext.current
    val categorySuccessfullyAdded =
        stringResource(R.string.category_operation_successfully_executed)
    val categoryCreationFailed = stringResource(R.string.category_operation_failed)
    TextButton(onClick = {

        if(categoryViewModel.getCategoryByIdentifier(category.identifier) != null) {
            return@TextButton
        }

        if(category.identifier.isEmpty()) {
            return@TextButton
        }

        categoryViewModel.upsertCategory(
            Category(
                category.uuid,
                category.identifier,
                category.createdAt,
                category.updatedAt
            ),
            movementWithCategoryViewModel,
            {
                DisplayToast.displayGeneric(context, categorySuccessfullyAdded)
                onSuccess()
            },
            {
                DisplayToast.displayGeneric(context, categoryCreationFailed)
                onFailure()
            }
        )
    }) {
        Text(text = stringResource(id = R.string.confirm))
    }
}