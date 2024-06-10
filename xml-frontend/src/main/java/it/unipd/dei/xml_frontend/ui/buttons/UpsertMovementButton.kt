package it.unipd.dei.xml_frontend.ui.buttons

import android.content.Context
import it.unipd.dei.common_backend.models.MovementBuilder
import it.unipd.dei.common_backend.utils.DisplayToast
import it.unipd.dei.common_backend.viewModels.CategoryViewModel
import it.unipd.dei.common_backend.viewModels.MovementWithCategoryViewModel

class UpsertMovementButton(
    private val movementBuilder: MovementBuilder,
    private val categoryViewModel: CategoryViewModel,
    private val movementWithCategoryViewModel: MovementWithCategoryViewModel,
    private val fragmentContext: Context,
    private val onClickRunnable: () -> Unit
) : IButton {

    override fun onClick() {
        movementBuilder.toMovement(categoryViewModel)?.let {
            movementWithCategoryViewModel.upsertMovement(
                it,
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
}