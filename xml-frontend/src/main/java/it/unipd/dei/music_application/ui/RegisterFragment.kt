package it.unipd.dei.music_application.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint
import it.unipd.dei.music_application.R

@AndroidEntryPoint
class RegisterFragment : Fragment() {
    private val viewModel : MovementWithCategoryViewModel by viewModels()
    private val testViewModel : TestViewModel by viewModels()
    private lateinit var adapter: MovementCardAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        //testViewModel.deleteAllMovements()
        testViewModel.createDummyDataIfNoMovement()
        val view = inflater.inflate(R.layout.fragment_register, container, false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.movements_recycler_view)
        val tabLayout = view.findViewById<TabLayout>(R.id.register_tab_layout)
        // Create the adapter with an empty list
        adapter = MovementCardAdapter(emptyList(), emptyList())

        // Set the adapter to the RecyclerView
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)

        // Chiamare il metodo del ViewModel per caricare i dati
        viewModel.getAllMovements()

        // Osservare i dati e aggiornare la UI
        viewModel.data.observe(viewLifecycleOwner, Observer { movements ->
            // Update the adapter with the new data
            adapter.updateMovements(movements)
        })

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                    adapter.filterMovements(tab?.text.toString())
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                // Azione quando un tab viene deselezionato
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                // Azione quando un tab viene nuovamente selezionato
            }
        })


        //TODO mettere un observer ai cambiamenti di stato di tabview
        /*
               val view = recyclerView.findViewHolderForAdapterPosition()

         */
        return view;
    }
}

