package it.unipd.dei.music_application.ui

import android.graphics.Color
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
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint
import it.unipd.dei.music_application.R
import it.unipd.dei.music_application.models.Category
import it.unipd.dei.music_application.models.CategoryTotal
import it.unipd.dei.music_application.models.MovementWithCategory
import it.unipd.dei.music_application.view.CategoryViewModel
import it.unipd.dei.music_application.view.MovementWithCategoryViewModel
import it.unipd.dei.music_application.view.TestViewModel

@AndroidEntryPoint
class RegisterFragment : Fragment() {

    // ViewModels
    private val testViewModel: TestViewModel by viewModels()
    private val movementWithCategoryViewModel: MovementWithCategoryViewModel by viewModels()
    private val categoryViewModel: CategoryViewModel by viewModels()

    // Adapters for RecyclerViews
    private lateinit var allRecyclerViewAdapter: MovementCardAdapter
    private lateinit var positiveRecyclerViewAdapter: MovementCardAdapter
    private lateinit var negativeRecyclerViewAdapter: MovementCardAdapter

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

    // Adapter for the dropdown menù
    private lateinit var arrayAdapter: ArrayAdapter<Category>

    // Loading state to prevent multiple loading
    private var loading = false

    //How many card we can see until it load more movements
    private val visibleThreshold = 3

    //
    private lateinit var selectedCategory: Category

    //PieChart in fragment_register
    private lateinit var pieChart: PieChart

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

        // Create dummy data if no movement exists
        testViewModel.createDummyDataIfNoMovement()

        // Inflate the layout
        val view = inflater.inflate(R.layout.fragment_register, container, false)

        // Initialize PieChart
        //TODO RIMUOVERE PIECHART SE NON SERVE anche dal fragment xml
        pieChart = view.findViewById<PieChart>(R.id.pie_chart)

        // Initialize RecyclerViews
        initializeRecyclerViews(view)

        // Initialize TabLayout
        initializeTabLayout(view)

        // Initialize TextView
        initializeTextView(view)

        // Initialize AutoCompleteTextView
        initializeAutoCompleteTextView(view)

        // Create the adapters with an empty list
        initializeAdapters()

        // Set the adapters and layout managers to the RecyclerViews
        setupRecyclerViews()

        // Observe data changes and update the UI
        observeViewModelData()

        // Call the ViewModel method to load initial data
        movementWithCategoryViewModel.loadInitialMovements()
        movementWithCategoryViewModel.loadTotalAmounts()
        categoryViewModel.loadCategoryTotal()
        categoryViewModel.getAllCategories()

        return view
    }

    override fun onPause() {
        super.onPause()
        //TODO salva in datastore o sharedpreference
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

    private fun initializeAdapters() {
        allRecyclerViewAdapter = MovementCardAdapter(emptyList())
        positiveRecyclerViewAdapter = MovementCardAdapter(emptyList())
        negativeRecyclerViewAdapter = MovementCardAdapter(emptyList())
    }

    private fun initializeAutoCompleteTextView(view: View) {
        autoCompleteTextView = view.findViewById(R.id.menu)
        autoCompleteTextView.setOnItemClickListener { parent, _, position, _ ->
            selectedCategory = parent.getItemAtPosition(position) as Category}
        //TODO mettere la categoria TUTTI
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
        movementWithCategoryViewModel.allData.observe(viewLifecycleOwner) { movements ->
            updateAdapter(allRecyclerViewAdapter, movements)
        }
        movementWithCategoryViewModel.positiveData.observe(viewLifecycleOwner) { movements ->
            updateAdapter(positiveRecyclerViewAdapter, movements)
        }
        movementWithCategoryViewModel.negativeData.observe(viewLifecycleOwner) { movements ->
            updateAdapter(negativeRecyclerViewAdapter, movements)
        }
        categoryViewModel.allData.observe(viewLifecycleOwner) { categories ->
            updatePieChart(categories)
        }
        movementWithCategoryViewModel.totalAllAmount.observe(viewLifecycleOwner) { amount ->
            updateTextContainer(amount, "totalAll")
        }
        movementWithCategoryViewModel.totalPositiveAmount.observe(viewLifecycleOwner) { amount ->
            updateTextContainer(amount, "totalPositive")
        }
        movementWithCategoryViewModel.totalNegativeAmount.observe(viewLifecycleOwner) { amount ->
            updateTextContainer(amount, "totalNegative")
        }
        categoryViewModel.allCategories.observe(viewLifecycleOwner) { categories ->
            updateDropdownMenu(categories)
        }
    }

    private fun addScrollListenerForAllRecycleViews() {
        addScrollListener(allRecyclerView) {
            movementWithCategoryViewModel.loadSomeMovements()
        }
        addScrollListener(positiveRecyclerView) {
            movementWithCategoryViewModel.loadSomePositiveMovements()
        }
        addScrollListener(negativeRecyclerView) {
            movementWithCategoryViewModel.loadSomeNegativeMovements()
        }

    }

    private fun addScrollListener(
        recyclerView: RecyclerView,
        loadMore: () -> Unit
    ) {
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (!loading && recyclerView.visibility == View.VISIBLE) {
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
        adapter.updateMovements(newMovements, startChangePosition, itemCount)
    }

    private fun updatePieChart(categoryTotals: List<CategoryTotal>) {

        val entries = ArrayList<PieEntry>()
        val colors = ArrayList<Int>()

        for (categoryTotal in categoryTotals) {
            entries.add(PieEntry(categoryTotal.totalAmount.toFloat(), categoryTotal.identifier))
            if (categoryTotal.totalAmount >= 0) {
                colors.add(Color.GREEN) // Colore per valori positivi
            } else {
                colors.add(Color.RED) // Colore per valori negativi
            }
        }

        val dataSet = PieDataSet(entries, "Movements by Category")
        dataSet.colors = colors

        val data = PieData(dataSet)
        pieChart.data = data
        pieChart.invalidate() // refresh
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
        arrayAdapter =
            context?.let { ArrayAdapter(it, android.R.layout.simple_dropdown_item_1line, categories) }!!
        autoCompleteTextView.setAdapter(arrayAdapter)

    }

}
