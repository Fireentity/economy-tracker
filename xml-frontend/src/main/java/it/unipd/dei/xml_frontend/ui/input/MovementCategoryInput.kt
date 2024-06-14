package it.unipd.dei.xml_frontend.ui.input

import android.widget.AdapterView
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.LifecycleOwner
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import com.google.android.material.textfield.TextInputLayout
import it.unipd.dei.common_backend.models.MovementWithCategory
import it.unipd.dei.common_backend.viewModels.CategoryViewModel

class MovementCategoryInput(
    private val categoryViewModel: CategoryViewModel,
    view: TextInputLayout,
    textView: MaterialAutoCompleteTextView,
    lifecycleOwner: LifecycleOwner,
    movement: MovementWithCategory? = null
) {

    private var category = movement?.category?.identifier ?: ""

    init {
        setItemsInUI(textView)
        categoryViewModel.allCategories.observe(lifecycleOwner) {
            setItemsInUI(textView)
        }
        textView.setOnItemClickListener { parent, _, position, _ ->
            category = parent.getItemAtPosition(position) as String
            view.clearFocus()
        }
        
    }

    fun getCategory(): String = category

    private fun setItemsInUI(view: MaterialAutoCompleteTextView) {
        val categories: Array<String> = categoryViewModel.allCategories
            .value?.values?.map { it.identifier }
            .orEmpty().toTypedArray()

        view.setSimpleItems(categories)

        if (category != "") {
            view.setText(category)
        }

    }

    fun onItemClick(parent: AdapterView<*>, position: Int) {
        category = parent.getItemAtPosition(position) as String
    }
}