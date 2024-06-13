package it.unipd.dei.xml_frontend.ui.input

import android.graphics.Color
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.LifecycleOwner
import com.google.android.material.search.SearchBar
import com.google.android.material.search.SearchView
import it.unipd.dei.common_backend.viewModels.CategoryViewModel
import it.unipd.dei.xml_frontend.ui.adapters.CategoryCardAdapter

class SearchInput(
    searchView: SearchView,
    searchBar: SearchBar,
    adapter: CategoryCardAdapter,
    categoryViewModel: CategoryViewModel,
    lifecycleOwner: LifecycleOwner
) {
    init {

        val searchLayout = searchView.layoutParams.height
        searchView.layoutParams.height = 0

        searchView.addTransitionListener { _, _, newState ->
            if (newState === SearchView.TransitionState.SHOWING) {
                searchView.layoutParams.height = searchLayout
            }
            if (newState === SearchView.TransitionState.HIDDEN) {
                searchView.layoutParams.height = 0
            }
        }


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