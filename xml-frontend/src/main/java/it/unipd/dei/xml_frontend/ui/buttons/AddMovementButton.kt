package it.unipd.dei.xml_frontend.ui.buttons

import android.content.Context
import it.unipd.dei.common_backend.models.MovementBuilder
import it.unipd.dei.common_backend.utils.DisplayToast
import it.unipd.dei.common_backend.view.MovementWithCategoryViewModel

class AddMovementButton(
    private val movementBuilder: MovementBuilder,
    private val onClickRunnable: () -> Unit,
    private val fragmentContext: Context,
    private val movementWithCategoryViewModel: MovementWithCategoryViewModel,
) : IButton {

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
        )
    }
}