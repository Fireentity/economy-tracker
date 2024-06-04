package it.unipd.dei.music_application.ui

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
import it.unipd.dei.music_application.interfaces.OnItemClickListener
import it.unipd.dei.music_application.models.Category
import it.unipd.dei.music_application.ui.dialog.CategoryInputDialogFragment
import it.unipd.dei.music_application.ui.dialog.OptionModalBottomSheetFragment
import it.unipd.dei.music_application.view.CategoryViewModel

@AndroidEntryPoint
class CategoryFragment : Fragment(), OnItemClickListener {

    private val categoryViewModel: CategoryViewModel by viewModels()

    private lateinit var categoriesRecyclerView: RecyclerView
    private var categoriesRecyclerViewAdapter: CategoryCardAdapter =
        CategoryCardAdapter(emptyList(), this)
    private lateinit var floatingActionButton: FloatingActionButton

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_categories, container, false)

        initializeViews(view)

        observeViewModel()
        categoryViewModel.getAllCategories()
        return view
    }

    private fun observeViewModel() {
        categoryViewModel.allCategories.observe(viewLifecycleOwner) {
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
        floatingActionButton = view.findViewById(R.id.floating_action_button)
        floatingActionButton.setOnClickListener {
            showCategoryInputDialogFragment()
        }
    }

    private fun showCategoryInputDialogFragment() {
        val title = context?.resources?.getString(R.string.new_category_title)
        val buttonText = context?.resources?.getString(R.string.new_category_button)
        val categoryInputDialogFragment = CategoryInputDialogFragment(title, buttonText)
        categoryInputDialogFragment.show(parentFragmentManager, "CategoryInputDialogFragment")
    }

    override fun onItemClick(category: Category) {
        val optionModalBottomSheetFragment =
            OptionModalBottomSheetFragment(null, category)
        optionModalBottomSheetFragment.show(parentFragmentManager, "OptionModalBottomSheetFragment")
    }

}