package it.unipd.dei.xml_frontend.ui.dialog

import android.content.Context
import android.content.res.Resources
import androidx.appcompat.app.AlertDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import it.unipd.dei.common_backend.models.Category
import it.unipd.dei.common_backend.utils.DisplayToast
import it.unipd.dei.common_backend.viewModels.CategoryViewModel
import it.unipd.dei.common_backend.viewModels.MovementWithCategoryViewModel
import it.unipd.dei.xml_frontend.R

class DeleteCategoryDialog(
    categoryViewModel: CategoryViewModel,
    movementWithCategoryViewModel: MovementWithCategoryViewModel,
    private val fragmentContext: Context,
    category: Category
) : IDialog {

    private val alertDialog: AlertDialog

    init {
        alertDialog = MaterialAlertDialogBuilder(fragmentContext).setTitle(
            fragmentContext.resources.getString(R.string.delete_category)
        )
            .setMessage(fragmentContext.resources.getString(R.string.are_you_sure_you_want_to_delete_this_category))
            .setNeutralButton(fragmentContext.resources.getString(R.string.cancel)) { _, _ -> }
            .setPositiveButton(fragmentContext.resources.getString(R.string.confirm)) { _, _ ->
                categoryViewModel.deleteCategory(
                    category,
                    movementWithCategoryViewModel,
                    {
                        DisplayToast.displayGeneric(
                            fragmentContext,
                            fragmentContext.getString(R.string.category_deleted_successfully)
                        )
                        categoryViewModel.invalidateCategoriesAndReload()
                    },
                    {
                        DisplayToast.displayGeneric(
                            fragmentContext,
                            fragmentContext.getString(R.string.category_deletion_failed)
                        )
                    },
                )
            }.create()
    }

    override fun getFragmentContext(): Context = fragmentContext

    override fun getResources(): Resources = fragmentContext.resources

    override fun show() = alertDialog.show()

    override fun dismiss() = alertDialog.dismiss()
}