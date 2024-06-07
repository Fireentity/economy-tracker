package it.unipd.dei.xml_frontend.ui.input

import android.widget.AdapterView
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import it.unipd.dei.common_backend.models.MovementWithCategory
import it.unipd.dei.common_backend.view.CategoryViewModel

class MovementCategoryInput(
    private val categoryViewModel: CategoryViewModel,
    view: MaterialAutoCompleteTextView,
    movement: MovementWithCategory? = null
) {

    private var category = movement?.category

    fun getCategory() = category

    init {
        val categories: Array<String> = categoryViewModel.allCategories
            .value?.values?.map { it.identifier }
            .orEmpty().toTypedArray()
        view.setSimpleItems(categories)
    }

    fun onItemClick(parent: AdapterView<*>, position: Int) {
        val categoryIdentifier = parent.getItemAtPosition(position) as String
        category = categoryViewModel.getCategoryByIdentifier(categoryIdentifier)
    }
}