package it.unipd.dei.xml_frontend.ui.dialog

import android.content.Context
import android.content.res.Resources
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.doOnTextChanged
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import dagger.hilt.android.AndroidEntryPoint
import it.unipd.dei.common_backend.models.Category
import it.unipd.dei.common_backend.view.CategoryViewModel
import it.unipd.dei.xml_frontend.R
import it.unipd.dei.xml_frontend.ui.buttons.UpdateCategoryButton
import it.unipd.dei.xml_frontend.ui.input.CategoryIdentifierInput

class EditCategoryDialog(
    category: Category,
    categoryViewModel: CategoryViewModel,
    view: View,
    private val fragmentContext: Context,
) : IDialog {

    private val alertDialog: AlertDialog
    private val inputField: CategoryIdentifierInput

    init {
        val inputFieldView: TextInputEditText = view.findViewById(R.id.input_category_identifier)
        val updateCategoryButton = UpdateCategoryButton(
            //TODO fix here the category is not modified
            category,
            categoryViewModel,
            fragmentContext,
            this::dismiss
        )

        this.inputField = CategoryIdentifierInput(
            this.fragmentContext.resources,
            categoryViewModel,
            inputFieldView,
            category
        )
        this.alertDialog = MaterialAlertDialogBuilder(fragmentContext)
            .setView(view)
            .setPositiveButton(
                R.string.confirm
            ) { _, _ -> updateCategoryButton.onClick() }
            .create()

        inputFieldView.doOnTextChanged { text, _, _, _ ->
            inputField.onTextChanged(text)
        }

        view.setOnClickListener {
            updateCategoryButton.onClick()
        }
    }

    override fun getFragmentContext() = fragmentContext

    override fun getResources(): Resources = fragmentContext.resources

    override fun show() = alertDialog.show()

    override fun dismiss() = alertDialog.dismiss()
}