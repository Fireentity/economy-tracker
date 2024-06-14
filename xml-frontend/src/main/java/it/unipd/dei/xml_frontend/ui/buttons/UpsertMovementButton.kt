package it.unipd.dei.xml_frontend.ui.buttons

import android.content.Context
import it.unipd.dei.common_backend.models.MovementBuilder
import it.unipd.dei.common_backend.utils.DisplayToast
import it.unipd.dei.common_backend.viewModels.CategoryViewModel
import it.unipd.dei.common_backend.viewModels.MovementWithCategoryViewModel
import it.unipd.dei.common_backend.viewModels.SummaryViewModel
import it.unipd.dei.xml_frontend.R

class UpsertMovementButton(
    private val movementBuilder: MovementBuilder,
    private val categoryViewModel: CategoryViewModel,
    private val movementWithCategoryViewModel: MovementWithCategoryViewModel,
    private val summaryViewModel: SummaryViewModel,
    private val fragmentContext: Context,
    private val onClickRunnable: () -> Unit
) : IButton {

    override fun onClick() {
        movementBuilder.toMovement(categoryViewModel)?.let {
            movementWithCategoryViewModel.upsertMovement(
                it,
                summaryViewModel,
                {
                    DisplayToast.displayGeneric(fragmentContext, fragmentContext.getString(R.string.movement_operation_successfully_executed))
                    onClickRunnable()
                },
                {
                    DisplayToast.displayGeneric(fragmentContext, fragmentContext.getString(R.string.movement_operation_failed))
                    onClickRunnable()
                }
            )
        }
    }
}