package it.unipd.dei.xml_frontend.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import it.unipd.dei.common_backend.viewModels.CategoryViewModel
import it.unipd.dei.xml_frontend.R
import it.unipd.dei.xml_frontend.ui.adapters.CategoryCardAdapter
import it.unipd.dei.xml_frontend.ui.buttons.ShowAddCategoryDialogButton

@AndroidEntryPoint
class CategoriesFragment : Fragment() {

    private val categoryViewModel: CategoryViewModel by viewModels()
    //TODO essenso diversi questo e quello del register bisogna trovare un modo per reloaddare i movimenti quando uno è stato eliminato con l'eliminazione della categoria

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
        val categoriesRecyclerView: RecyclerView = view.findViewById(R.id.all_categories_recycler_view)
        categoriesRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        val floatingActionButton: View = view.findViewById(R.id.show_add_category_dialog_button)
        val showAddCategoryDialogButton = ShowAddCategoryDialogButton(
            dialogView,
            categoryViewModel,
            requireContext()
        )
        floatingActionButton.setOnClickListener {
            showAddCategoryDialogButton.onClick()
        }

        categoriesRecyclerView.adapter = categoryViewModel.allCategories.value?.values?.toList()
            ?.let { CategoryCardAdapter(it, parentFragmentManager, categoryViewModel) }
        categoryViewModel.allCategories.observe(viewLifecycleOwner){
            categoriesRecyclerView.adapter = CategoryCardAdapter(it.values.toList(), parentFragmentManager, categoryViewModel)
        }
        categoryViewModel.loadAllCategories()
        return view
    }

}