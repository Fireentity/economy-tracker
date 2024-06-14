package it.unipd.dei.xml_frontend.ui.models

import android.content.Context
import it.unipd.dei.common_backend.models.Category
import it.unipd.dei.common_backend.utils.DisplayToast
import it.unipd.dei.common_backend.view.CategoryViewModel
import it.unipd.dei.xml_frontend.R
import java.util.UUID

class AddCategoryButton(
    private val categoryIdentifierSupplier: () -> String,
    private val onClickRunnable: () -> Unit,
    private val fragmentContext: Context,
    private val categoryViewModel: CategoryViewModel,
) : IButton {

    override fun onClick() {

        val category = categoryViewModel.getCategoryByIdentifier(categoryIdentifierSupplier())
        if (category != null) {
            DisplayToast.displayGeneric(
                fragmentContext,
                fragmentContext.resources.getString(R.string.category_already_exists)
            )
            return
        }

        categoryViewModel.upsertCategory(
            Category(
                UUID.randomUUID(),
                categoryIdentifierSupplier(),
                System.currentTimeMillis(),
                System.currentTimeMillis()
            ),
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