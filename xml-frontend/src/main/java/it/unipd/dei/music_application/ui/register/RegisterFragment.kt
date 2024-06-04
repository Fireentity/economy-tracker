package it.unipd.dei.music_application.ui.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint
import it.unipd.dei.music_application.R
import it.unipd.dei.music_application.enums.CategoryTab
import it.unipd.dei.music_application.models.Category
import it.unipd.dei.music_application.models.MovementWithCategory
import it.unipd.dei.music_application.ui.MovementCardAdapter
import it.unipd.dei.music_application.ui.OnItemLongClickListener
import it.unipd.dei.music_application.ui.dialog.DialogInputFragment
import it.unipd.dei.music_application.ui.dialog.OptionModalBottomSheetFragment
import it.unipd.dei.music_application.view.CategoryViewModel
import it.unipd.dei.music_application.view.MovementWithCategoryViewModel
import it.unipd.dei.music_application.view.TestViewModel

@AndroidEntryPoint
class RegisterFragment : Fragment(), OnItemLongClickListener {

    // ViewModels
    private val testViewModel: TestViewModel by viewModels()
    private val movementWithCategoryViewModel: MovementWithCategoryViewModel by viewModels()
    private val categoryViewModel: CategoryViewModel by viewModels()

    private lateinit var register: Register

    // AutoCompleteTextView in fragment_register layout
    private lateinit var autoCompleteTextView: AutoCompleteTextView

    // FloatingActionButton in fragment_register layout
    private lateinit var floatingActionButton: FloatingActionButton

    // Adapter for the dropdown menù
    private lateinit var arrayAdapter: ArrayAdapter<Category>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

        // Create dummy data if no movement exists
        testViewModel.createDummyDataIfNoMovement()

        // Inflate the layout
        val view = inflater.inflate(R.layout.fragment_register, container, false)

        register = Register(
            listOf(
                RegisterTab(
                    movementWithCategoryViewModel,
                    view.findViewById(R.id.all_movements_recycler_view),
                    MovementCardAdapter(movementWithCategoryViewModel.getMovements(), this, viewLifecycleOwner)
                ),
                RegisterTab(
                    movementWithCategoryViewModel,
                    view.findViewById(R.id.positive_movements_recycler_view),
                    MovementCardAdapter(movementWithCategoryViewModel.getPositiveMovement(), this, viewLifecycleOwner),
                ),
                RegisterTab(
                    movementWithCategoryViewModel,
                    view.findViewById(R.id.negative_movements_recycler_view),
                    MovementCardAdapter(movementWithCategoryViewModel.getNegativeMovement(), this, viewLifecycleOwner),
                )
            ),
            view.findViewById(R.id.register_tab_layout)
        )

        // Initialize AutoCompleteTextView
        initializeAutoCompleteTextView(view)

        // Initialize FloatingActionButton
        initializeFloatingActionButton(view)

        // Observe data changes and update the UI
        observeViewModelData()

        // Call the ViewModel method to load initial data
        movementWithCategoryViewModel.loadInitialMovementsByCategory(selectedCategory)
        movementWithCategoryViewModel.loadTotalAmountsByCategory(selectedCategory)
        categoryViewModel.getAllCategories()
        return view
    }

    private fun initializeFloatingActionButton(view: View) {
        floatingActionButton = view.findViewById(R.id.floating_action_button)
        floatingActionButton.setOnClickListener {
            showDialogFragment()
        }
    }

    private fun showDialogFragment() {
        val dialogFragment = DialogInputFragment()
        dialogFragment.show(parentFragmentManager, "DialogInputFragment")
    }

    override fun onPause() {
        super.onPause()
        //TODO salva in datastore o sharedpreference:
        //ultima categoria vista? cambia anche updateDropdownMenu
        //ultimo pannello visto (entrate/uscite/tutti)
    }

    override fun onResume() {
        super.onResume()
        //TODO leggi da datastore o sharedpreference
        //tabLayout.getTabAt(defaultSelectedTabLayoutPosition)?.select()
    }

    private fun initializeAutoCompleteTextView(view: View) {
        autoCompleteTextView = view.findViewById(R.id.menu)
        autoCompleteTextView.setOnItemClickListener { parent, _, position, _ ->
            autoCompleteTextView.clearFocus()
            val temp = parent.getItemAtPosition(position) as Category
            if (selectedCategory != temp) {

                loading = true
                selectedCategory = temp

                allRecyclerViewAdapter.clear()
                positiveRecyclerViewAdapter.clear()
                negativeRecyclerViewAdapter.clear()

                movementWithCategoryViewModel.loadInitialMovementsByCategory(selectedCategory)
                movementWithCategoryViewModel.loadTotalAmountsByCategory(selectedCategory)

                loading = false
            }
        }
    }

    private fun observeViewModelData() {
        movementWithCategoryViewModel.getMovements().observe(viewLifecycleOwner) {
            updateAdapter(allRecyclerViewAdapter, it)
        }
        movementWithCategoryViewModel.getPositiveMovement().observe(viewLifecycleOwner) {
            updateAdapter(positiveRecyclerViewAdapter, it)
        }
        movementWithCategoryViewModel.getNegativeMovement().observe(viewLifecycleOwner) {
            updateAdapter(negativeRecyclerViewAdapter, it)
        }
        categoryViewModel.allCategories.observe(viewLifecycleOwner) {
            updateDropdownMenu(it)
        }
    }

    private fun updateDropdownMenu(categories: List<Category>) {
        arrayAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_dropdown_item_1line,
            listOf(
                generalCategory
            ) + categories
        )

        autoCompleteTextView.setAdapter(arrayAdapter)

        // Di default sono tutte le categorie
        autoCompleteTextView.setText(ALL_CATEGORIES_IDENTIFIER, false)
        autoCompleteTextView.setSelection(0)
    }


    override fun onItemLongClick(position: Int) {
        var movementWithCategory: MovementWithCategory? = null
        //PORCATA MA AVENDO 3 recyclerview o faccio 3 adapter diversi o cosi
        when (tabLayout.selectedTabPosition) {
            //ENTRATE
            0 -> {
                movementWithCategory =
                    movementWithCategoryViewModel.positiveData.value?.get(position)
            }
            //TUTTI
            1 -> {
                movementWithCategory =
                    movementWithCategoryViewModel.allData.value?.get(position)
            }
            //USCITE
            2 -> {
                movementWithCategory =
                    movementWithCategoryViewModel.negativeData.value?.get(position)
            }
        }
        if (movementWithCategory == null) {
            return
        }
        val optionModalBottomSheetFragment =
            OptionModalBottomSheetFragment(movementWithCategory, null)
        optionModalBottomSheetFragment.show(parentFragmentManager, "DialogInputFragment")
    }

}