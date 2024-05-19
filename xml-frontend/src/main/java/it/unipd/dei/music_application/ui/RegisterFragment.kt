package it.unipd.dei.music_application.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint
import it.unipd.dei.music_application.R
import kotlin.math.abs

@AndroidEntryPoint
class RegisterFragment : Fragment() {

    // ViewModels
    private val testViewModel: TestViewModel by viewModels()
    private val movementWithCategoryViewModel: MovementWithCategoryViewModel by viewModels()

    // Adapters for RecyclerViews
    private lateinit var allAdapter: MovementCardAdapter
    private lateinit var positiveAdapter: MovementCardAdapter
    private lateinit var negativeAdapter: MovementCardAdapter

    // RecyclerViews in fragment_register layout
    private lateinit var allRecyclerView: RecyclerView
    private lateinit var positiveRecyclerView: RecyclerView
    private lateinit var negativeRecyclerView: RecyclerView

    //
    private lateinit var nestedScrollView: NestedScrollView

    // TabLayout in fragment_register layout
    private lateinit var tabLayout: TabLayout

    // Loading state to prevent multiple loading
    private var loading = false

    //How many card we can see until it load more movements
    private val visibleThreshold = 3
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        // Create dummy data if no movement exists
        testViewModel.createDummyDataIfNoMovement()

        // Inflate the layout
        val view = inflater.inflate(R.layout.fragment_register, container, false)

        // Initialize RecyclerViews
        initializeRecyclerViews(view)

        // Initialize TabLayout
        initializeTabLayout(view)

        // Create the adapters with an empty list
        initializeAdapters()

        // Set the adapters and layout managers to the RecyclerViews
        setupRecyclerViews()

        // Call the ViewModel method to load initial data
        movementWithCategoryViewModel.getMovements()

        // Observe data changes and update the UI
        observeViewModelData()
        return view
    }

    private fun initializeRecyclerViews(view: View) {
        allRecyclerView = view.findViewById(R.id.all_movements_recycler_view)
        positiveRecyclerView = view.findViewById(R.id.positive_movements_recycler_view)
        negativeRecyclerView = view.findViewById(R.id.negative_movements_recycler_view)
        addScrollListenerForAllRecycleViews()
    }

    private fun initializeTabLayout(view: View) {
        tabLayout = view.findViewById(R.id.register_tab_layout)
        addTabItemListener()
    }

    private fun initializeAdapters() {
        allAdapter = MovementCardAdapter(emptyList())
        positiveAdapter = MovementCardAdapter(emptyList())
        negativeAdapter = MovementCardAdapter(emptyList())
    }

    private fun setupRecyclerViews() {

        allRecyclerView.apply {
            adapter = allAdapter
            layoutManager = LinearLayoutManager(context)
        }
        positiveRecyclerView.apply {
            adapter = positiveAdapter
            layoutManager = LinearLayoutManager(context)
        }
        negativeRecyclerView.apply {
            adapter = negativeAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun observeViewModelData() {
        movementWithCategoryViewModel.allData.observe(viewLifecycleOwner) { movements ->
            allAdapter.updateMovements(movements)
        }
        movementWithCategoryViewModel.positiveData.observe(viewLifecycleOwner) { movements ->
            positiveAdapter.updateMovements(movements)
        }
        movementWithCategoryViewModel.negativeData.observe(viewLifecycleOwner) { movements ->
            negativeAdapter.updateMovements(movements)
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
                        if (allAdapter.itemCount > 0) {
                            allRecyclerView.scrollToPosition(0)
                        }
                    }

                    "Uscite" -> {
                        if (negativeAdapter.itemCount > 0) {
                            negativeRecyclerView.scrollToPosition(0)
                        }
                    }

                    "Entrate" -> {
                        if (positiveAdapter.itemCount > 0) {
                            positiveRecyclerView.scrollToPosition(0)
                        }
                    }
                }
            }
        })
    }
}
