package it.unipd.dei.jetpack_compose_frontend.ui.dialog

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import it.unipd.dei.common_backend.models.MovementWithCategory
import it.unipd.dei.common_backend.view.MovementWithCategoryViewModel
import it.unipd.dei.jetpack_compose_frontend.R

@Composable
fun DeleteMovementDialog(
    movement: MovementWithCategory,
    movementWithCategoryViewModel: MovementWithCategoryViewModel,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        onDismissRequest = { },
        title = { Text(text = stringResource(R.string.delete_movement)) },
        text = { Text(text = stringResource(R.string.movement_deletion_confirmation)) },
        confirmButton = {
            TextButton(onClick = {
                movementWithCategoryViewModel.deleteMovement(
                    movement.movement,
                    onConfirm
                ) { onDismiss() }
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