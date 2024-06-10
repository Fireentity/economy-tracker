package it.unipd.dei.jetpack_compose_frontend.ui.buttons

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import it.unipd.dei.common_backend.models.MovementBuilder
import it.unipd.dei.common_backend.utils.DisplayToast
import it.unipd.dei.common_backend.viewModels.CategoryViewModel
import it.unipd.dei.common_backend.viewModels.MovementWithCategoryViewModel
import it.unipd.dei.jetpack_compose_frontend.R

@Composable
fun AddMovementButton(
    movementBuilder: MovementBuilder,
    movementWithCategoryViewModel: MovementWithCategoryViewModel,
    categoryViewModel: CategoryViewModel
) {
    val context = LocalContext.current
    val categorySuccessfullyAdded = stringResource(R.string.movement_added_successfully)
    val categoryCreationFailed = stringResource(R.string.movement_creation_failed)
    Button(onClick = {
        movementWithCategoryViewModel.upsertMovement(
            movementBuilder.toMovement(categoryViewModel),
            {
                DisplayToast.displayGeneric(context, categorySuccessfullyAdded)
            },
            {
                DisplayToast.displayGeneric(context, categoryCreationFailed)
            }
        )
    }) {
        Text(text = stringResource(R.string.save))
    }
}