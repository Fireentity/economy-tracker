package it.unipd.dei.xml_frontend.ui.dialog

import android.content.Context
import android.content.res.Resources
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.doOnTextChanged
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import it.unipd.dei.common_backend.models.Category
import it.unipd.dei.common_backend.viewModels.CategoryViewModel
import it.unipd.dei.xml_frontend.R
import it.unipd.dei.xml_frontend.ui.buttons.UpsertCategoryButton
import it.unipd.dei.xml_frontend.ui.input.CategoryIdentifierInput

class UpsertCategoryDialog(
    categoryViewModel: CategoryViewModel,
    view: View,
    private val fragmentContext: Context,
    title: String,
    category: Category? = null,
) : IDialog {

    private val alertDialog: AlertDialog
    private val identifierInputField: CategoryIdentifierInput

    init {
        val inputFieldView: TextInputEditText = view.findViewById(R.id.input_category_identifier)
        val updateCategoryButton = UpsertCategoryButton(
            categoryViewModel,
            fragmentContext,
            this::dismiss,
            { getText() },
            category
        )

        this.identifierInputField = CategoryIdentifierInput(
            this.fragmentContext.resources,
            categoryViewModel,
            inputFieldView,
            category
        )
        this.alertDialog = MaterialAlertDialogBuilder(fragmentContext)
            .setTitle(title)
            .setView(view)
            .setPositiveButton(
                R.string.confirm
            ) { _, _ -> updateCategoryButton.onClick() }
            .create()

        inputFieldView.doOnTextChanged { text, _, _, _ ->
            identifierInputField.onTextChanged(text)
        }

        view.setOnClickListener {
            updateCategoryButton.onClick()
        }
    }

    private fun getText(): String? = identifierInputField.getText()

    override fun getFragmentContext() = fragmentContext

    override fun getResources(): Resources = fragmentContext.resources

    override fun show() = alertDialog.show()

    override fun dismiss() = alertDialog.dismiss()
}