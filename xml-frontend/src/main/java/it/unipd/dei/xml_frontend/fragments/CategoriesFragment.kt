package it.unipd.dei.xml_frontend.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import it.unipd.dei.common_backend.view.CategoryViewModel
import it.unipd.dei.xml_frontend.R
import it.unipd.dei.xml_frontend.ui.CategoryCardAdapter
import it.unipd.dei.xml_frontend.ui.buttons.ShowAddCategoryDialogButton

@AndroidEntryPoint
class CategoriesFragment : Fragment() {

    private val categoryViewModel: CategoryViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(
            R.layout.fragment_categories,
            container,
            false
        )
        val dialogView = inflater.inflate(
            R.layout.fragment_category_dialog,
            container,
            false
        )
        val categoriesRecyclerView: View = view.findViewById(R.id.all_categories_recycler_view)
        val floatingActionButton: View = view.findViewById(R.id.show_add_category_dialog_button)
        val showAddCategoryDialogButton = ShowAddCategoryDialogButton(
            dialogView,
            categoryViewModel,
            requireContext()
        )
        floatingActionButton.setOnClickListener {
            showAddCategoryDialogButton.onClick()
        }

        val categoriesRecyclerViewAdapter = CategoryCardAdapter(emptyList(), parentFragmentManager)

        categoryViewModel.loadAllCategories()
        return view
    }

}