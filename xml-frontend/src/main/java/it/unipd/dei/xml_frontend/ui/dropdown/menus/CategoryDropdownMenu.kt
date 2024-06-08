package it.unipd.dei.xml_frontend.ui.dropdown.menus

import android.content.Context
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.lifecycle.LifecycleOwner
import it.unipd.dei.common_backend.models.Category
import it.unipd.dei.common_backend.view.CategoryViewModel
import it.unipd.dei.common_backend.view.MovementWithCategoryViewModel
import it.unipd.dei.xml_frontend.R

class CategoryDropdownMenu(
    private val categoryViewModel: CategoryViewModel,
    private val movementWithCategoryViewModel: MovementWithCategoryViewModel,
    private val autoCompleteTextView: AutoCompleteTextView,
    lifecycleOwner: LifecycleOwner,
    private val context: Context
) {
    private var selectedCategoryIdentifier: String =
        context.getString(R.string.all_categories_identifier)

    init {
        categoryViewModel.allCategories.value?.let { setAdapter(it) }
        categoryViewModel.allCategories.observe(lifecycleOwner) {
            setAdapter(it)
        }
        autoCompleteTextView.setText(context.getString(R.string.all_categories_identifier), false)
        autoCompleteTextView.setSelection(0)

        autoCompleteTextView.setOnItemClickListener { parent, _, position, _ ->
            autoCompleteTextView.clearFocus()
            val temp = parent.getItemAtPosition(position) as String
            if (selectedCategoryIdentifier != temp) {
                selectedCategoryIdentifier = temp
                if (selectedCategoryIdentifier == context.getString(R.string.all_categories_identifier)) {
                    movementWithCategoryViewModel.removeCategoryFilter()
                } else {
                    categoryViewModel.getCategoryByIdentifier(selectedCategoryIdentifier)
                        ?.let { movementWithCategoryViewModel.addCategoryFilter(it) }
                }

                //TODO vedi se questo è il posto giusto per queste due chiamate a funzione o se:
                movementWithCategoryViewModel.invalidateMovements()
                movementWithCategoryViewModel.loadInitialMovementsByCategory()
            }
        }
    }

    private fun setAdapter(categories: MutableMap<String, Category>) {
        autoCompleteTextView.setAdapter(
            ArrayAdapter(
                context,
                android.R.layout.simple_dropdown_item_1line,
                listOf(context.getString(R.string.all_categories_identifier)) + categories.keys.toList()
            )
        )
    }
}
