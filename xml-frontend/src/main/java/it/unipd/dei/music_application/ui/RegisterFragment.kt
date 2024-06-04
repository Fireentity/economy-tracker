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
import it.unipd.dei.music_application.ui.dialog.OptionModalBottomSheetFragment
import it.unipd.dei.music_application.utils.Constants.ALL_CATEGORIES_IDENTIFIER
import it.unipd.dei.music_application.view.CategoryViewModel
import it.unipd.dei.music_application.view.MovementWithCategoryViewModel
import it.unipd.dei.music_application.view.TestViewModel
import java.util.UUID

@AndroidEntryPoint
class RegisterFragment : Fragment(), OnItemLongClickListener {

    // ViewModels
    private val testViewModel: TestViewModel by viewModels()
    private val movementWithCategoryViewModel: MovementWithCategoryViewModel by viewModels()
    private val categoryViewModel: CategoryViewModel by viewModels()

    // Adapters for RecyclerViews
    private var allRecyclerViewAdapter = MovementCardAdapter(emptyList(), this)
    private var positiveRecyclerViewAdapter = MovementCardAdapter(emptyList(), this)
    private var negativeRecyclerViewAdapter = MovementCardAdapter(emptyList(), this)

    // RecyclerViews in fragment_register layout
    private lateinit var allRecyclerView: RecyclerView
    private lateinit var positiveRecyclerView: RecyclerView
    private lateinit var negativeRecyclerView: RecyclerView

    //TextView for displaying amounts in fragment_register layout
    private lateinit var totalAllTextView: TextView
    private lateinit var totalPositiveTextView: TextView
    private lateinit var totalNegativeTextView: TextView

    // TabLayout in fragment_register layout
    private lateinit var tabLayout: TabLayout
    private var defaultSelectedTabLayoutPosition: Int = 1

    // AutoCompleteTextView in fragment_register layout
    private lateinit var autoCompleteTextView: AutoCompleteTextView

    // FloatingActionButton in fragment_register layout
    private lateinit var floatingActionButton: FloatingActionButton

    // Adapter for the dropdown menù
    private lateinit var arrayAdapter: ArrayAdapter<Category>

    // Loading state to prevent multiple loading
    private var loading = false

    // How many card we cannot see until it load more movements
    private val visibleThreshold = 3

    // Utils for latest view Category and the default Category with all movements
    private val generalCategory =
        Category(
            UUID.randomUUID(),
            ALL_CATEGORIES_IDENTIFIER,
            System.currentTimeMillis(),
            System.currentTimeMillis()
        )
    private var selectedCategory = generalCategory


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

        // Create dummy data if no movement exists
        testViewModel.createDummyDataIfNoMovement()

        // Inflate the layout
        val view = inflater.inflate(R.layout.fragment_register, container, false)

        // Initialize RecyclerViews
        initializeRecyclerViews(view)

        // Initialize TabLayout
        initializeTabLayout(view)

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
        categoryViewModel.getAllCategories()
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

    private fun initializeRecyclerViews(view: View) {
        allRecyclerView = view.findViewById(R.id.all_movements_recycler_view)
        positiveRecyclerView = view.findViewById(R.id.positive_movements_recycler_view)
        negativeRecyclerView = view.findViewById(R.id.negative_movements_recycler_view)
        addScrollListenerForAllRecycleViews()
    }

    private fun initializeTabLayout(view: View) {
        tabLayout = view.findViewById(R.id.register_tab_layout)
        tabLayout.getTabAt(defaultSelectedTabLayoutPosition)?.select();
        addTabItemListener()
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

    private fun setupRecyclerViews() {

        allRecyclerView.apply {
            adapter = allRecyclerViewAdapter
            layoutManager = LinearLayoutManager(context)
        }
        positiveRecyclerView.apply {
            adapter = positiveRecyclerViewAdapter
            layoutManager = LinearLayoutManager(context)
        }
        negativeRecyclerView.apply {
            adapter = negativeRecyclerViewAdapter
            layoutManager = LinearLayoutManager(context)
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
        categoryViewModel.allCategories.observe(viewLifecycleOwner) {
            if (it != null) {
                updateDropdownMenu(it)
            }
        }
    }

    private fun addScrollListenerForAllRecycleViews() {
        addScrollListener(allRecyclerView) {
            movementWithCategoryViewModel.loadSomeMovementsByCategory(selectedCategory)
        }
        addScrollListener(positiveRecyclerView) {
            movementWithCategoryViewModel.loadSomePositiveMovementsByCategory(selectedCategory)
        }
        addScrollListener(negativeRecyclerView) {
            movementWithCategoryViewModel.loadSomeNegativeMovementsByCategory(selectedCategory)
        }

    }

    private fun addScrollListener(
        recyclerView: RecyclerView,
        loadMore: () -> Unit
    ) {
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0 && !loading && recyclerView.visibility == View.VISIBLE) {
                    super.onScrolled(recyclerView, dx, dy)
                    val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                    val totalItemCount = layoutManager.itemCount
                    val lastVisibleItem = layoutManager.findLastVisibleItemPosition()
                    if (totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                        loading = true
                        loadMore()
                        loading = false
                    }
                }
            }
        })

    }

    private fun addTabItemListener() {
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.text) {
                    "Tutti" -> {
                        allRecyclerView.visibility = View.VISIBLE
                        positiveRecyclerView.visibility = View.GONE
                        negativeRecyclerView.visibility = View.GONE
                    }

                    "Uscite" -> {
                        allRecyclerView.visibility = View.GONE
                        positiveRecyclerView.visibility = View.GONE
                        negativeRecyclerView.visibility = View.VISIBLE
                    }

                    "Entrate" -> {
                        allRecyclerView.visibility = View.GONE
                        positiveRecyclerView.visibility = View.VISIBLE
                        negativeRecyclerView.visibility = View.GONE
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                when (tab?.text) {
                    "Tutti" -> allRecyclerView.visibility = View.GONE
                    "Uscite" -> negativeRecyclerView.visibility = View.GONE
                    "Entrate" -> positiveRecyclerView.visibility = View.GONE
                }
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                // On tab reselected it scroll himself to the first element (if present)
                when (tab?.text) {
                    "Tutti" -> {
                        if (allRecyclerViewAdapter.itemCount > 0) {
                            allRecyclerView.scrollToPosition(0)
                        }
                    }

                    "Uscite" -> {
                        if (negativeRecyclerViewAdapter.itemCount > 0) {
                            negativeRecyclerView.scrollToPosition(0)
                        }
                    }

                    "Entrate" -> {
                        if (positiveRecyclerViewAdapter.itemCount > 0) {
                            positiveRecyclerView.scrollToPosition(0)
                        }
                    }
                }
            }
        })
    }

    /*Call the more optimized adapter function that loads only the new movements into the recycleView*/
    private fun updateAdapter(
        adapter: MovementCardAdapter,
        newMovements: List<MovementWithCategory>
    ) {
        val startChangePosition = adapter.getMovementsCount()
        val itemCount = newMovements.size - startChangePosition
        if (itemCount > 0) {
            adapter.updateMovements(newMovements, startChangePosition, itemCount)
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
        optionModalBottomSheetFragment.show(parentFragmentManager, "OptionModalBottomSheetFragment")
    }

}
