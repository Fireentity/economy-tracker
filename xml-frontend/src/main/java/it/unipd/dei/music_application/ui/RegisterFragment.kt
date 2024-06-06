package it.unipd.dei.music_application.ui

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
import it.unipd.dei.music_application.interfaces.OnItemLongClickListener
import it.unipd.dei.music_application.models.Category
import it.unipd.dei.music_application.models.MovementWithCategory
import it.unipd.dei.music_application.ui.dialog.MovementInputDialogFragment
import it.unipd.dei.music_application.ui.dialog.OptionMovementModalBottomSheetFragment
import it.unipd.dei.music_application.ui.register.Register
import it.unipd.dei.music_application.ui.register.RegisterTab
import it.unipd.dei.music_application.view.CategoryViewModel
import it.unipd.dei.music_application.view.MovementWithCategoryViewModel
import it.unipd.dei.music_application.view.TestViewModel
import java.util.UUID

@AndroidEntryPoint
class RegisterFragment : Fragment() {

    // ViewModels
    private val testViewModel: TestViewModel by viewModels()
    private val movementWithCategoryViewModel: MovementWithCategoryViewModel by viewModels()
    private val categoryViewModel: CategoryViewModel by viewModels()
    //TextView for displaying amounts in fragment_register layout
    private lateinit var totalAllTextView: TextView
    private lateinit var totalPositiveTextView: TextView
    private lateinit var totalNegativeTextView: TextView

    // AutoCompleteTextView in fragment_register layout
    private lateinit var autoCompleteTextView: AutoCompleteTextView

    // FloatingActionButton in fragment_register layout
    private lateinit var floatingActionButton: FloatingActionButton

    // Adapter for the dropdown menù
    private lateinit var arrayAdapter: ArrayAdapter<Category>

    // Loading state to prevent multiple loading
    private lateinit var register: Register;

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
                    MovementCardAdapter(
                        movementWithCategoryViewModel.getMovements().value?: emptyList(),
                        parentFragmentManager
                    )
                ),
                RegisterTab(
                    movementWithCategoryViewModel,
                    view.findViewById(R.id.positive_movements_recycler_view),
                    MovementCardAdapter(
                        movementWithCategoryViewModel.getPositiveMovement().value?: emptyList(),
                        parentFragmentManager
                    )
                ),
                RegisterTab(
                    movementWithCategoryViewModel,
                    view.findViewById(R.id.negative_movements_recycler_view),
                    MovementCardAdapter(
                        movementWithCategoryViewModel.getNegativeMovement().value?: emptyList(),
                        parentFragmentManager
                    )
                )
            ),
            view.findViewById(R.id.register_tab_layout)
        )

        // Initialize TextView
        initializeTextView(view)

        // Initialize AutoCompleteTextView
        initializeAutoCompleteTextView(view)

        // Initialize FloatingActionButton
        initializeFloatingActionButton(view)


        // Set the adapters and layout managers to the RecyclerViews
        setupRecyclerViews()

        // Observe data changes and update the UI
        observeViewModelData()

        // Call the ViewModel method to load initial data
        movementWithCategoryViewModel.loadInitialMovementsByCategory(selectedCategory)
        movementWithCategoryViewModel.loadTotalAmountsByCategory(selectedCategory)
        categoryViewModel.loadAllCategories()
        return view
    }

    private fun initializeFloatingActionButton(view: View) {
        floatingActionButton = view.findViewById(R.id.floating_action_button)
        floatingActionButton.setOnClickListener {
            showMovementInputDialogFragment()
        }
    }

    private fun showMovementInputDialogFragment() {
        val title = context?.resources?.getString(R.string.new_movement_title)
        val buttonText = context?.resources?.getString(R.string.new_movement_button)
        val movementInputDialogFragment = MovementInputDialogFragment(title, buttonText)
        movementInputDialogFragment.show(parentFragmentManager, "MovementInputDialogFragment")
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

    private fun initializeTextView(view: View) {
        totalPositiveTextView = view.findViewById(R.id.text_top_left_recycler_view)
        totalNegativeTextView = view.findViewById(R.id.text_top_right_recycler_view)
        totalAllTextView = view.findViewById(R.id.text_top_center_recycler_view)
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
        movementWithCategoryViewModel.allData.observe(viewLifecycleOwner) {
            if (it != null) {
                updateAdapter(allRecyclerViewAdapter, it)
            }
        }
        movementWithCategoryViewModel.positiveData.observe(viewLifecycleOwner) {
            if (it != null) {
                updateAdapter(positiveRecyclerViewAdapter, it)
            }
        }
        movementWithCategoryViewModel.negativeData.observe(viewLifecycleOwner) {
            if (it != null) {
                updateAdapter(negativeRecyclerViewAdapter, it)
            }
        }
        movementWithCategoryViewModel.totalAllAmount.observe(viewLifecycleOwner) {
            if (it != null) {
                updateTextContainer(it, "totalAll")
            }
        }
        movementWithCategoryViewModel.totalPositiveAmount.observe(viewLifecycleOwner) {
            if (it != null) {
                updateTextContainer(it, "totalPositive")
            }
        }
        movementWithCategoryViewModel.totalNegativeAmount.observe(viewLifecycleOwner) {
            if (it != null) {
                updateTextContainer(it, "totalNegative")
            }
        }
        categoryViewModel.loadAllCategories.observe(viewLifecycleOwner) {
            if (it != null) {
                updateDropdownMenu(it)
            }
        }
    }

    private fun updateTextContainer(amount: Double, type: String) {

        when (type) {
            "totalAll" -> {
                totalAllTextView.text = String.format("%.2f", amount)
                val euroView = view?.findViewById<TextView>(R.id.euro_symbol_center)
                if (euroView != null) {
                    val colorRes = if (amount > 0) R.color.green_600 else R.color.rose_600
                    val color = ContextCompat.getColor(requireContext(), colorRes)
                    euroView.setTextColor(color)
                    totalAllTextView.setTextColor(color)
                }
            }

            "totalPositive" -> {
                totalPositiveTextView.text = String.format("%.2f", amount)
            }

            "totalNegative" -> {
                totalNegativeTextView.text = String.format("%.2f", amount)
            }
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
}
