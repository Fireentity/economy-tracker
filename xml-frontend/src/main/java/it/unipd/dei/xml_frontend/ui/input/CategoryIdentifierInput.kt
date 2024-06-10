package it.unipd.dei.xml_frontend.ui.input

import android.content.res.Resources
import com.google.android.material.textfield.TextInputEditText
import it.unipd.dei.common_backend.models.Category
import it.unipd.dei.common_backend.viewModels.CategoryViewModel
import it.unipd.dei.xml_frontend.R

class CategoryIdentifierInput(
    private val resources: Resources,
    private val categoryViewModel: CategoryViewModel,
    private val view: TextInputEditText,
    category: Category? = null
) {

    private var text = category?.identifier

    fun getText(): String? = text

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