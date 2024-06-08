package it.unipd.dei.xml_frontend.ui.input

import android.widget.AdapterView
import androidx.lifecycle.LifecycleOwner
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import com.google.android.material.textfield.TextInputLayout
import it.unipd.dei.common_backend.models.Category
import it.unipd.dei.common_backend.models.MovementWithCategory
import it.unipd.dei.common_backend.view.CategoryViewModel

class MovementCategoryInput(
    private val categoryViewModel: CategoryViewModel,
    view: TextInputLayout,
    textView: MaterialAutoCompleteTextView,
    lifecycleOwner: LifecycleOwner,
    movement: MovementWithCategory? = null
) {

    private var category = movement?.category

    fun getCategory() = category

    init {
        setItemsInUI(textView)
        categoryViewModel.allCategories.observe(lifecycleOwner) {
            setItemsInUI(textView)
        }
        textView.setOnItemClickListener { parent, _, position, _ ->
            category = categoryViewModel.getCategoryByIdentifier(parent.getItemAtPosition(position) as String)
            view.clearFocus()
        }
    }

    private fun setItemsInUI(view: MaterialAutoCompleteTextView) {
        val categories: Array<String> = categoryViewModel.allCategories
            .value?.values?.map { it.identifier }
            .orEmpty().toTypedArray()

        view.setSimpleItems(categories)

        if (category != null) {
            view.setText(category!!.identifier)
        }

    }

    fun onItemClick(parent: AdapterView<*>, position: Int) {
        val categoryIdentifier = parent.getItemAtPosition(position) as String
        category = categoryViewModel.getCategoryByIdentifier(categoryIdentifier)
    }
}