package it.unipd.dei.xml_frontend.ui.dialog.category

import android.content.Context
import android.content.DialogInterface
import android.content.res.Resources
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.doOnTextChanged
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import dagger.hilt.android.AndroidEntryPoint
import it.unipd.dei.common_backend.models.Category
import it.unipd.dei.xml_frontend.ui.models.CategoryIdentifierField
import it.unipd.dei.xml_frontend.ui.models.EditCategoryButton
import it.unipd.dei.common_backend.view.CategoryViewModel
import it.unipd.dei.xml_frontend.R

@AndroidEntryPoint
class EditCategoryDialog(
    category: Category,
    categoryViewModel: CategoryViewModel,
    view: View,
    private val fragmentContext: Context,
) : IDialog {

    private val alertDialog: AlertDialog
    private val inputField: CategoryIdentifierField

    init {
        val inputFieldView: TextInputEditText = view.findViewById(R.id.input_category_identifier)
        val editCategoryButton = EditCategoryButton(
            category,
            categoryViewModel,
            fragmentContext,
            this::dismiss
        )

        this.inputField = CategoryIdentifierField(
            this.fragmentContext.resources,
            categoryViewModel,
            inputFieldView
        )
        this.alertDialog = MaterialAlertDialogBuilder(fragmentContext)
            .setView(view)
            .setPositiveButton(
                R.string.confirm,
                DialogInterface.OnClickListener { _, _ -> editCategoryButton.onClick() })
            .create()

        inputFieldView.doOnTextChanged { text, _, _, _ ->
            inputField.onTextChanged(text)
        }

        view.setOnClickListener {
            editCategoryButton.onClick()
        }
    }

    override fun getFragmentContext() = fragmentContext

    override fun getResources(): Resources = fragmentContext.resources

    override fun show() = alertDialog.show()

    override fun dismiss() = alertDialog.dismiss()
}