package it.unipd.dei.xml_frontend.ui.dialog

import android.content.Context
import android.content.res.Resources
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.doOnTextChanged
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import it.unipd.dei.common_backend.view.CategoryViewModel
import it.unipd.dei.xml_frontend.R
import it.unipd.dei.xml_frontend.ui.buttons.AddCategoryButton
import it.unipd.dei.xml_frontend.ui.input.CategoryIdentifierInput

class AddCategoryDialog(
    categoryViewModel: CategoryViewModel,
    view: View,
    private val fragmentContext: Context,
) : IDialog {

    private val alertDialog: AlertDialog
    private val inputField: CategoryIdentifierInput

    init {
        val inputFieldView: TextInputEditText = view.findViewById(R.id.input_category_identifier)
        val editCategoryButton = AddCategoryButton(
            this::getCategoryIdentifier,
            this::dismiss,
            fragmentContext,
            categoryViewModel
        )

        this.inputField = CategoryIdentifierInput(
            fragmentContext.resources,
            categoryViewModel,
            inputFieldView
        )
        this.alertDialog = MaterialAlertDialogBuilder(fragmentContext)
            .setTitle("ciao")
            .setView(view)
            .setPositiveButton(
                R.string.confirm
            ) { _, _ -> editCategoryButton.onClick() }
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

    override fun dismiss() {
        alertDialog.dismiss()
    }

    private fun getCategoryIdentifier(): String? = inputField.getText()
}