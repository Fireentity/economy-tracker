package it.unipd.dei.music_application.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint
import it.unipd.dei.music_application.R

@AndroidEntryPoint
class RegisterFragment : Fragment() {
    //test
    private val testViewModel: TestViewModel by viewModels()

    //viewModel for accessing movements and their category
    private val viewModel: MovementWithCategoryViewModel by viewModels()

    //adapters for every RecycleView
    private lateinit var allAdapter: MovementCardAdapter
    private lateinit var positiveAdapter: MovementCardAdapter
    private lateinit var negativeAdapter: MovementCardAdapter

    //RecycleViews in fragment_register
    private lateinit var allRecyclerView: RecyclerView
    private lateinit var positiveRecyclerView: RecyclerView
    private lateinit var negativeRecyclerView: RecyclerView

    //TabItem in fragment_register
    private lateinit var tabLayout: TabLayout

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        //testViewModel.deleteAllMovements()
        testViewModel.createDummyDataIfNoMovement()
        val view = inflater.inflate(R.layout.fragment_register, container, false)

        //RecycleView initialization
        allRecyclerView = view.findViewById(R.id.all_movements_recycler_view)
        positiveRecyclerView = view.findViewById(R.id.positive_movements_recycler_view)
        negativeRecyclerView = view.findViewById(R.id.negative_movements_recycler_view)

        //tabLayout initialization
        tabLayout = view.findViewById<TabLayout>(R.id.register_tab_layout)

        // Create the adapter with an empty list
        allAdapter = MovementCardAdapter(emptyList())
        positiveAdapter = MovementCardAdapter(emptyList())
        negativeAdapter = MovementCardAdapter(emptyList())

        // Set the adapter to the RecyclerView
        allRecyclerView.adapter = allAdapter
        allRecyclerView.layoutManager = LinearLayoutManager(context)

        positiveRecyclerView.adapter = positiveAdapter
        positiveRecyclerView.layoutManager = LinearLayoutManager(context)

        negativeRecyclerView.adapter = negativeAdapter
        negativeRecyclerView.layoutManager = LinearLayoutManager(context)

        // Chiamare il metodo del ViewModel per caricare i dati
        viewModel.getMovements()
        // Osservare i dati e aggiornare la UI
        viewModel.allData.observe(viewLifecycleOwner) { movements ->
            // Update the adapter with the new data
            allAdapter.updateMovements(movements)
        }
        viewModel.positiveData.observe(viewLifecycleOwner) { movements ->
            // Update the adapter with the new data
            positiveAdapter.updateMovements(movements)
        }
        viewModel.negativeData.observe(viewLifecycleOwner) { movements ->
            // Update the adapter with the new data
            negativeAdapter.updateMovements(movements)
        }
        this.addTabItemListener()
        return view;
    }

    private fun addTabItemListener(){
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.text) {
                    "Tutti" -> allRecyclerView.visibility = View.VISIBLE
                    "Uscite" -> negativeRecyclerView.visibility = View.VISIBLE
                    "Entrate" -> positiveRecyclerView.visibility = View.VISIBLE
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                // Azione quando un tab viene deselezionato
                when (tab?.text) {
                    "Tutti" -> allRecyclerView.visibility = View.GONE
                    "Uscite" -> negativeRecyclerView.visibility = View.GONE
                    "Entrate" -> positiveRecyclerView.visibility = View.GONE
                }
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }
}

