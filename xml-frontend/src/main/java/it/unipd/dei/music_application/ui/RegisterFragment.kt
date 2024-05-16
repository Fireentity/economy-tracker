package it.unipd.dei.music_application.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import dagger.hilt.android.AndroidEntryPoint
import it.unipd.dei.music_application.R
import it.unipd.dei.music_application.models.MovementWithCategory

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
        // Create the adapter with an empty list
        adapter = MovementCardAdapter(emptyList())

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
        return view;
    }
}
/*val chart = view.findViewById<PieChart>(R.id.pie_chart);
        val movements = database.getMovementDao().getAllMovements();
        val recycler = view.findViewById<RecyclerView>(R.id.movements_recycler_view);

        val visitors: ArrayList<PieEntry> = arrayListOf(
            PieEntry(500F,"value2")
        )
        chart.data = PieData(PieDataSet(visitors, "test"))*/