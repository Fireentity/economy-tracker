package it.unipd.dei.xml_frontend.ui.buttons

import android.content.Context
import android.view.View
import it.unipd.dei.common_backend.view.CategoryViewModel
import it.unipd.dei.xml_frontend.ui.dialog.AddCategoryDialog

class ShowAddCategoryDialogButton(
    private val dialogView: View,
    private val categoryViewModel: CategoryViewModel,
    private val fragmentContext: Context
) : IButton {

    override fun onClick() {
        AddCategoryDialog(categoryViewModel,dialogView,fragmentContext).show()
    }
}