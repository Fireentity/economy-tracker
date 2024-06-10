package it.unipd.dei.xml_frontend.ui.buttons

import android.content.Context
import it.unipd.dei.common_backend.models.Category
import it.unipd.dei.common_backend.viewModels.CategoryViewModel
import it.unipd.dei.common_backend.viewModels.MovementWithCategoryViewModel
import it.unipd.dei.xml_frontend.ui.dialog.DeleteCategoryDialog

class ShowDeleteCategoryDialogButton(
    private val category: Category,
    private val categoryViewModel: CategoryViewModel,
    private val movementWithCategoryViewModel: MovementWithCategoryViewModel,
    private val fragmentContext: Context
) : IButton {

    override fun onClick() {
        DeleteCategoryDialog(categoryViewModel, fragmentContext, category)
    }
}