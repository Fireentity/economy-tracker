package it.unipd.dei.jetpack_compose_frontend.ui.buttons

import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import it.unipd.dei.common_backend.models.MovementBuilder
import it.unipd.dei.common_backend.utils.DisplayToast
import it.unipd.dei.common_backend.viewModels.CategoryViewModel
import it.unipd.dei.common_backend.viewModels.MovementWithCategoryViewModel
import it.unipd.dei.common_backend.viewModels.SummaryViewModel
import it.unipd.dei.jetpack_compose_frontend.R

@Composable
fun AddMovementButton(
    movementBuilder: MovementBuilder,
    movementWithCategoryViewModel: MovementWithCategoryViewModel,
    categoryViewModel: CategoryViewModel,
    summaryViewModel: SummaryViewModel,
    onClick: () -> Unit
) {
    val context = LocalContext.current
    val movementSuccessfullyAdded = stringResource(R.string.movement_operation_successfully_executed)
    val movementCreationFailed = stringResource(R.string.movement_operation_failed)
    TextButton(onClick = {
        movementBuilder.toMovement(categoryViewModel)?.let {
            movementWithCategoryViewModel.upsertMovement(
                it,
                summaryViewModel,
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