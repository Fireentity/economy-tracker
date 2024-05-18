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
    private val viewModel: MovementWithCategoryViewModel by viewModels()
    private val testViewModel: TestViewModel by viewModels()
    private lateinit var allAdapter: MovementCardAdapter
    private lateinit var positiveAdapter: MovementCardAdapter
    private lateinit var negativeAdapter: MovementCardAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        //testViewModel.deleteAllMovements()
        testViewModel.createDummyDataIfNoMovement()
        val view = inflater.inflate(R.layout.fragment_register, container, false)
        val allRecyclerView =
            view.findViewById<RecyclerView>(R.id.all_movements_recycler_view)
        val positiveRecyclerView =
            view.findViewById<RecyclerView>(R.id.positive_movements_recycler_view)
        val negativeRecyclerView =
            view.findViewById<RecyclerView>(R.id.negative_movements_recycler_view)

        val tabLayout = view.findViewById<TabLayout>(R.id.register_tab_layout)

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
        return view;
    }
}

