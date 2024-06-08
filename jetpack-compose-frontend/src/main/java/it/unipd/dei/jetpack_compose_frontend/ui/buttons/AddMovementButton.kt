package it.unipd.dei.jetpack_compose_frontend.ui.buttons

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import it.unipd.dei.common_backend.models.MovementBuilder
import it.unipd.dei.common_backend.utils.DisplayToast
import it.unipd.dei.common_backend.view.MovementWithCategoryViewModel
import it.unipd.dei.jetpack_compose_frontend.R

@Composable
fun AddMovementButton(
    movementBuilder: MovementBuilder,
    movementWithCategoryViewModel: MovementWithCategoryViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val categorySuccessfullyAdded = stringResource(R.string.movement_added_successfully)
    val categoryCreationFailed = stringResource(R.string.movement_creation_failed)
    Button(onClick = {
        movementBuilder.toMovement()?.let {
            movementWithCategoryViewModel.upsertMovement(
                it,
                {
                    DisplayToast.displayGeneric(context, categorySuccessfullyAdded)
                },
                {
                    DisplayToast.displayGeneric(context, categoryCreationFailed)
                }
            )
        }
     }) {
        Text(text = stringResource(R.string.save))
    }
}