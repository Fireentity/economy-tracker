package it.unipd.dei.jetpack_compose_frontend.ui.dialog

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import it.unipd.dei.common_backend.models.MovementWithCategory
import it.unipd.dei.common_backend.utils.DisplayToast
import it.unipd.dei.common_backend.viewModels.MovementWithCategoryViewModel
import it.unipd.dei.common_backend.viewModels.SummaryViewModel
import it.unipd.dei.jetpack_compose_frontend.R

@Composable
fun DeleteMovementDialog(
    movement: MovementWithCategory,
    movementWithCategoryViewModel: MovementWithCategoryViewModel,
    summaryViewModel: SummaryViewModel,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    val context = LocalContext.current
    val movementSuccessfullyAdded = stringResource(R.string.movement_deleted_successfully)
    val movementCreationFailed = stringResource(R.string.movement_deletion_failed)
    AlertDialog(
        onDismissRequest = { },
        title = { Text(text = stringResource(R.string.delete_movement)) },
        text = { Text(text = stringResource(R.string.are_you_sure_you_want_to_delete_this_movement)) },
        confirmButton = {
            TextButton(onClick = {
                movementWithCategoryViewModel.deleteMovement(
                    movement,
                    summaryViewModel,
                    {
                        DisplayToast.displayGeneric(context, movementSuccessfullyAdded)
                        onConfirm()
                    }
                ) {
                    DisplayToast.displayGeneric(context, movementCreationFailed)
                    onDismiss()
                }
            }) {
                Text(text = stringResource(R.string.delete))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = stringResource(R.string.cancel))
            }
        }
    )
}