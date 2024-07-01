package it.unipd.dei.xml_frontend.ui.input

import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.LifecycleOwner
import com.google.android.material.search.SearchView
import it.unipd.dei.common_backend.viewModels.CategoryViewModel
import it.unipd.dei.xml_frontend.ui.adapters.CategoryCardAdapter

class SearchInput(
    searchView: SearchView,
    adapter: CategoryCardAdapter,
    categoryViewModel: CategoryViewModel,
    lifecycleOwner: LifecycleOwner
) {
    init {
        searchView.editText.addTextChangedListener { text ->
            val allCategories = categoryViewModel.allCategories.value?.values?.toList()
                ?: return@addTextChangedListener

            val filteredCategories = if (text.isNullOrEmpty()) {
                allCategories
            } else {
                allCategories.filter { category ->
                    category.identifier.contains(text.toString(), ignoreCase = true)
                }
            }
            adapter.updateCategories(filteredCategories)
        }

        categoryViewModel.allCategories.observe(lifecycleOwner) {
            val allCategories = it.values.toList()
            val filteredCategories = if (searchView.editText.text.isNullOrEmpty()) {
                allCategories
            } else {
                allCategories.filter { category ->
                    category.identifier.contains(searchView.editText.toString(), ignoreCase = true)
                }
            }
            adapter.updateCategories(filteredCategories)
        }
    }
}