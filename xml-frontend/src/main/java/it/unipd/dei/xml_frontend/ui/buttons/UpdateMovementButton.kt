package it.unipd.dei.xml_frontend.ui.buttons

import android.content.Context
import it.unipd.dei.common_backend.models.MovementWithCategory
import it.unipd.dei.common_backend.utils.DisplayToast
import it.unipd.dei.common_backend.view.MovementWithCategoryViewModel

class UpdateMovementButton(
    private val movement: MovementWithCategory,
    private val movementWithCategoryViewModel: MovementWithCategoryViewModel,
    private val fragmentContext: Context,
    private val onClickRunnable: () -> Unit
) : IButton {

    override fun onClick() {
        movementWithCategoryViewModel.upsertMovement(
            movement.movement,
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