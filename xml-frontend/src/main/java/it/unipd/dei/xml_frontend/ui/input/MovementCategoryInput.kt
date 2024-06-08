package it.unipd.dei.xml_frontend.ui.input

import android.widget.AdapterView
import androidx.lifecycle.LifecycleOwner
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import it.unipd.dei.common_backend.models.MovementWithCategory
import it.unipd.dei.common_backend.view.CategoryViewModel

class MovementCategoryInput(
    private val categoryViewModel: CategoryViewModel,
    view: MaterialAutoCompleteTextView,
    lifecycleOwner: LifecycleOwner,
    movement: MovementWithCategory? = null
) {

    private var category = movement?.category

    fun getCategory() = category

    init {
        setItemsInUI(view)
        categoryViewModel.allCategories.observe(lifecycleOwner) {
            setItemsInUI(view)
        }
    }

    private fun setItemsInUI(view: MaterialAutoCompleteTextView) {
        val categories: Array<String> = categoryViewModel.allCategories
            .value?.values?.map { it.identifier }
            .orEmpty().toTypedArray()

        view.setSimpleItems(categories)

        if (category == null) {
            return
        }

        val index = categories.indexOf(category!!.identifier)
        if (index != -1) {
            view.setText(category!!.identifier)
        }
    }

    fun onItemClick(parent: AdapterView<*>, position: Int) {
        val categoryIdentifier = parent.getItemAtPosition(position) as String
        category = categoryViewModel.getCategoryByIdentifier(categoryIdentifier)
    }
}