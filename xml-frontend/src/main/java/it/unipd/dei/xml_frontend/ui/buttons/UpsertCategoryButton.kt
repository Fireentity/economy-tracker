package it.unipd.dei.xml_frontend.ui.buttons

import android.content.Context
import it.unipd.dei.common_backend.models.Category
import it.unipd.dei.common_backend.utils.DisplayToast
import it.unipd.dei.common_backend.viewModels.CategoryViewModel
import it.unipd.dei.common_backend.viewModels.MovementWithCategoryViewModel
import it.unipd.dei.xml_frontend.R
import java.util.UUID

class UpsertCategoryButton(
    private val categoryViewModel: CategoryViewModel,
    private val movementWithCategoryViewModel: MovementWithCategoryViewModel,
    private val fragmentContext: Context,
    private val onClickRunnable: () -> Unit,
    private val categoryIdentifierSupplier: () -> String?,
    private val oldCategory: Category? = null
) : IButton {

    override fun onClick() {
        val categoryIdentifier = categoryIdentifierSupplier() ?: return

        // Check if the category already exists
        if (categoryViewModel.getCategoryByIdentifier(categoryIdentifier) != null) {
            DisplayToast.displayGeneric(
                fragmentContext,
                fragmentContext.resources.getString(R.string.category_already_exists)
            )
            return
        }

        // Create a new category or update the old one
        val newCategory = oldCategory?.copy(
            identifier = categoryIdentifier,
            updatedAt = System.currentTimeMillis()
        ) ?: Category(
            UUID.randomUUID(),
            categoryIdentifier,
            System.currentTimeMillis(),
            System.currentTimeMillis()
        )

        // Upsert the category
        categoryViewModel.upsertCategory(
            newCategory,
            movementWithCategoryViewModel,
            {
                DisplayToast.displayGeneric(fragmentContext, fragmentContext.getString(R.string.category_operation_successfully_executed))
                onClickRunnable()
            },
            {
                DisplayToast.displayGeneric(fragmentContext, fragmentContext.getString(R.string.category_operation_failed))
                onClickRunnable()
            }
        )
    }
}
