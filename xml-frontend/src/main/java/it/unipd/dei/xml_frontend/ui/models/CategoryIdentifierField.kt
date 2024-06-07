package it.unipd.dei.xml_frontend.ui.models

import android.content.res.Resources
import com.google.android.material.textfield.TextInputEditText
import it.unipd.dei.common_backend.view.CategoryViewModel
import it.unipd.dei.xml_frontend.R

class CategoryIdentifierField(
    private val resources: Resources,
    private val categoryViewModel: CategoryViewModel,
    private val view: TextInputEditText
) {

    private var text = ""

    fun getText() = text

    fun onTextChanged(text: CharSequence?) {
        this.text = text.toString()
        val category = categoryViewModel.getCategoryByIdentifier(text.toString())
        if (category != null) {
            view.error = resources.getString(R.string.category_already_exists)
            return
        }

        view.error = null
    }
}