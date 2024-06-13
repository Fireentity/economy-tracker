package it.unipd.dei.xml_frontend.ui.dropdown.menus

import android.content.Context
import androidx.lifecycle.LifecycleOwner
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import it.unipd.dei.common_backend.viewModels.CategoryViewModel
import it.unipd.dei.common_backend.viewModels.MovementWithCategoryViewModel
import it.unipd.dei.xml_frontend.R

class CategoryDropdownMenu(
    private val categoryViewModel: CategoryViewModel,
    private val movementWithCategoryViewModel: MovementWithCategoryViewModel,
    private val autoCompleteTextView: MaterialAutoCompleteTextView,
    lifecycleOwner: LifecycleOwner,
    private val context: Context
) {
    private var selectedCategoryIdentifier: String =
        context.getString(R.string.all)
    init {
        val allCategoryIdentifier = context.getString(R.string.all)
        categoryViewModel.allCategories.value?.let {
            autoCompleteTextView.setSimpleItems(
                arrayOf(allCategoryIdentifier) + it.keys.toTypedArray()
            )
        }

        categoryViewModel.allCategories.observe(lifecycleOwner) {
            autoCompleteTextView.setSimpleItems(
                arrayOf(allCategoryIdentifier) + it.keys.toTypedArray()
            )
        }
        autoCompleteTextView.setText(allCategoryIdentifier, false)
        autoCompleteTextView.setSelection(0)

        autoCompleteTextView.setOnItemClickListener { parent, _, position, _ ->
            autoCompleteTextView.clearFocus()
            val temp = parent.getItemAtPosition(position) as String
            if (selectedCategoryIdentifier == temp) {
                return@setOnItemClickListener
            }

            selectedCategoryIdentifier = temp
            if (selectedCategoryIdentifier == allCategoryIdentifier) {
                movementWithCategoryViewModel.removeCategoryFilter()
            } else {
                categoryViewModel.getCategoryByIdentifier(selectedCategoryIdentifier)
                    ?.let { movementWithCategoryViewModel.addCategoryFilter(it) }
            }
            movementWithCategoryViewModel.invalidateMovementsAndReload()
        }
    }
}
