package it.unipd.dei.xml_frontend.ui.buttons

import android.content.Context
import android.view.View
import it.unipd.dei.common_backend.viewModels.CategoryViewModel
import it.unipd.dei.common_backend.viewModels.MovementWithCategoryViewModel
import it.unipd.dei.xml_frontend.R
import it.unipd.dei.xml_frontend.ui.dialog.UpsertCategoryDialog

class ShowAddCategoryDialogButton(
    dialogView: View,
    categoryViewModel: CategoryViewModel,
    movementWithCategoryViewModel: MovementWithCategoryViewModel,
    fragmentContext: Context
) : IButton {
    private val addCategoryDialog = UpsertCategoryDialog(
        categoryViewModel,
        movementWithCategoryViewModel,
        dialogView,
        fragmentContext,
        fragmentContext.getString(R.string.create_category)
    )

    override fun onClick() {
        addCategoryDialog.show()
    }
}