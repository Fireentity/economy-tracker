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
    categoryViewModel: CategoryViewModel,
    onClick: () -> Unit
) {
    val context = LocalContext.current
    val movementSuccessfullyAdded = stringResource(R.string.movement_added_successfully)
    val movementCreationFailed = stringResource(R.string.movement_creation_failed)
    Button(onClick = {
        movementBuilder.toMovement(categoryViewModel)?.let {
            movementWithCategoryViewModel.upsertMovement(
                it,
                {
                    DisplayToast.displayGeneric(context, movementSuccessfullyAdded)
                    onClick()
                },
                {
                    DisplayToast.displayGeneric(context, movementCreationFailed)
                    onClick()
                }
            )
        }
    }) {
        Text(text = stringResource(R.string.save))
    }
}