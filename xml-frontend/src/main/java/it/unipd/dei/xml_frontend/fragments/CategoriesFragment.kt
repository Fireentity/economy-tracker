package it.unipd.dei.xml_frontend.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dagger.hilt.android.AndroidEntryPoint
import it.unipd.dei.music_application.R
import it.unipd.dei.common_backend.models.Category
import it.unipd.dei.xml_frontend.ui.CategoryCardAdapter
import it.unipd.dei.xml_frontend.ui.models.ShowAddCategoryDialogButton
import it.unipd.dei.common_backend.view.CategoryViewModel

@AndroidEntryPoint
class CategoriesFragment : Fragment() {

    private val categoryViewModel: CategoryViewModel by viewModels()

    private lateinit var categoriesRecyclerView: RecyclerView
    private var categoriesRecyclerViewAdapter: CategoryCardAdapter =
        CategoryCardAdapter(emptyList())
    private lateinit var floatingActionButton: FloatingActionButton

    private val textToSearch = "y";

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_categories, container, false)
        val floatingActionButton: View = view.findViewById(R.id.floating_action_button)
        val showAddCategoryDialogButton = ShowAddCategoryDialogButton(null,categoryViewModel,requireContext())
        floatingActionButton.setOnClickListener {
            showAddCategoryDialogButton.onClick()
        }

        initializeViews(view)

        observeViewModel()
        categoryViewModel.loadAllCategories()
        return view
    }

    private fun observeViewModel() {
        categoryViewModel.loadAllCategories.observe(viewLifecycleOwner) {
            if (it != null) {
                updateAdapter(it)
            }
        }
    }

    private fun updateAdapter(categories: List<Category>) {
        categoriesRecyclerViewAdapter.updateCategories(categories)
    }

    private fun initializeViews(view: View) {
        categoriesRecyclerView = view.findViewById(R.id.all_categories_recycler_view)
        categoriesRecyclerView.apply {
            adapter = categoriesRecyclerViewAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }
}