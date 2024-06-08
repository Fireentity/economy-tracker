package it.unipd.dei.xml_frontend.ui.buttons

import android.content.Context
import it.unipd.dei.common_backend.models.MovementBuilder
import it.unipd.dei.common_backend.models.MovementWithCategory
import it.unipd.dei.common_backend.utils.DisplayToast
import it.unipd.dei.common_backend.view.MovementWithCategoryViewModel

class UpdateMovementButton(
    private val movementBuilder: MovementBuilder,
    private val movementWithCategoryViewModel: MovementWithCategoryViewModel,
    private val fragmentContext: Context,
    private val onClickRunnable: () -> Unit
) : IButton {

    //TODO uguale a AddMovementButton
    override fun onClick() {
        val movement  = movementBuilder.toMovement()
        if (movement == null) {
            DisplayToast.displayFailure(fragmentContext)
            return
        }

        movementWithCategoryViewModel.upsertMovement(
            movement,
            {
                DisplayToast.displaySuccess(fragmentContext)
                onClickRunnable()
            },
            {
                DisplayToast.displayFailure(fragmentContext)
                onClickRunnable()
            }
        );
    }
}