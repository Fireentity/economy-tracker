package it.unipd.dei.xml_frontend.ui.buttons

import android.content.Context
import it.unipd.dei.common_backend.models.Category
import it.unipd.dei.common_backend.utils.DisplayToast
import it.unipd.dei.common_backend.view.CategoryViewModel

class UpdateCategoryButton(
    private val category: Category,
    private val categoryViewModel: CategoryViewModel,
    private val fragmentContext: Context,
    private val onClickRunnable: () -> Unit
) : IButton {

    override fun onClick() {
        categoryViewModel.upsertCategory(
            category,
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