package it.unipd.dei.xml_frontend.ui.buttons

import android.content.Context
import android.view.View
import it.unipd.dei.common_backend.view.CategoryViewModel
import it.unipd.dei.xml_frontend.ui.dialog.AddCategoryDialog

class ShowAddCategoryDialogButton(
    dialogView: View,
    categoryViewModel: CategoryViewModel,
    fragmentContext: Context
) : IButton {
    private val addCategoryDialog = AddCategoryDialog(categoryViewModel,dialogView,fragmentContext)

    override fun onClick() {
        addCategoryDialog.show()
    }
}