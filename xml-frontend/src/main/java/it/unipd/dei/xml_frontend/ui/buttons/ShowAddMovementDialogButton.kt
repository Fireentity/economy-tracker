package it.unipd.dei.xml_frontend.ui.buttons

import android.content.Context
import android.view.View
import androidx.lifecycle.LifecycleOwner
import it.unipd.dei.common_backend.viewModels.CategoryViewModel
import it.unipd.dei.common_backend.viewModels.MovementWithCategoryViewModel
import it.unipd.dei.xml_frontend.R
import it.unipd.dei.xml_frontend.ui.dialog.UpsertMovementDialog

class ShowAddMovementDialogButton(
    dialogView: View,
    categoryViewModel: CategoryViewModel,
    movementWithCategoryViewModel: MovementWithCategoryViewModel,
    fragmentContext: Context,
    lifecycleOwner: LifecycleOwner
) : IButton {

    private val addMovementDialog = UpsertMovementDialog(
        categoryViewModel,
        movementWithCategoryViewModel,
        dialogView,
        fragmentContext,
        lifecycleOwner,
        fragmentContext.getString(R.string.new_movement_title)
    )

    override fun onClick() {
        addMovementDialog.show()
    }
}