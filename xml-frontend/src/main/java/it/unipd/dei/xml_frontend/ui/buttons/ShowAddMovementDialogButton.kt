package it.unipd.dei.xml_frontend.ui.buttons

import android.content.Context
import android.view.View
import it.unipd.dei.common_backend.view.CategoryViewModel
import it.unipd.dei.common_backend.view.MovementWithCategoryViewModel
import it.unipd.dei.xml_frontend.ui.dialog.AddMovementDialog

class ShowAddMovementDialogButton(
    private val dialogView: View,
    private val categoryViewModel: CategoryViewModel,
    private val movementWithCategoryViewModel: MovementWithCategoryViewModel,
    private val fragmentContext: Context
) : IButton {

    override fun onClick() {
        AddMovementDialog(
            categoryViewModel,
            movementWithCategoryViewModel,
            dialogView,
            fragmentContext
        ).show()
    }
}