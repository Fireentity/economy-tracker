package it.unipd.dei.xml_frontend.ui.buttons

import android.content.Context
import android.view.View
import it.unipd.dei.common_backend.view.CategoryViewModel
import it.unipd.dei.common_backend.view.MovementWithCategoryViewModel
import it.unipd.dei.xml_frontend.ui.dialog.AddMovementDialog

class ShowAddMovementDialogButton(
    dialogView: View,
    categoryViewModel: CategoryViewModel,
    movementWithCategoryViewModel: MovementWithCategoryViewModel,
    fragmentContext: Context
) : IButton {

    private val addMovementDialog = AddMovementDialog(
        categoryViewModel,
        movementWithCategoryViewModel,
        dialogView,
        fragmentContext
    )

    override fun onClick() {
        addMovementDialog.show()
    }
}