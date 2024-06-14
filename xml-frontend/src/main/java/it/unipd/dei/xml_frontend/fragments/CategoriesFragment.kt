package it.unipd.dei.xml_frontend.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import it.unipd.dei.common_backend.viewModels.CategoryViewModel
import it.unipd.dei.common_backend.viewModels.MovementWithCategoryViewModel
import it.unipd.dei.xml_frontend.MainActivity
import it.unipd.dei.xml_frontend.R
import it.unipd.dei.xml_frontend.ui.adapters.CategoryCardAdapter
import it.unipd.dei.xml_frontend.ui.buttons.ShowAddCategoryDialogButton
import it.unipd.dei.xml_frontend.ui.buttons.ShowDrawerButton
import it.unipd.dei.xml_frontend.ui.input.SearchInput


@AndroidEntryPoint
class CategoriesFragment : Fragment() {

    private val categoryViewModel: CategoryViewModel by activityViewModels()
    private val movementWithCategoryViewModel: MovementWithCategoryViewModel by activityViewModels()

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
        val categoriesRecyclerView: RecyclerView =
            view.findViewById(R.id.all_categories_recyclerview)

        val floatingActionButton: View = view.findViewById(R.id.show_add_category_dialog_button)
        val showAddCategoryDialogButton = ShowAddCategoryDialogButton(
            dialogView,
            categoryViewModel,
            movementWithCategoryViewModel,
            requireContext()
        )
        floatingActionButton.setOnClickListener {
            showAddCategoryDialogButton.onClick()
        }

        val categoryCardAdapter =
            CategoryCardAdapter(emptyList(), parentFragmentManager, categoryViewModel)
        categoriesRecyclerView.adapter = categoryCardAdapter
        categoryViewModel.allCategories.observe(viewLifecycleOwner) {
            categoryCardAdapter.updateCategories(it.values.toList())
        }

        val searchRecyclerView: RecyclerView = view.findViewById(R.id.search_results_recyclerview)

        val searchRecyclerViewAdapter = CategoryCardAdapter(
            emptyList(),
            parentFragmentManager,
            categoryViewModel
        )
        searchRecyclerView.adapter = searchRecyclerViewAdapter

        SearchInput(
            view.findViewById(R.id.search_view),
            view.findViewById(R.id.search_bar),
            searchRecyclerViewAdapter,
            categoryViewModel,
            viewLifecycleOwner
        )
        categoryViewModel.loadAllCategories()


        val menuIcon = view.findViewById<View>(R.id.menu_icon_button)
        val showDrawerButton = ShowDrawerButton(requireActivity() as MainActivity)
        menuIcon.setOnClickListener {
            showDrawerButton.onClick()
        }
        return view
    }

}