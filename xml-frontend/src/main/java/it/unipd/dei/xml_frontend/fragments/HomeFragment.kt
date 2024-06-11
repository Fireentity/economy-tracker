package it.unipd.dei.xml_frontend.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import it.unipd.dei.common_backend.viewModels.SummaryCardViewModel
import it.unipd.dei.xml_frontend.R
import it.unipd.dei.xml_frontend.ui.adapters.SummaryCardAdapter

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private val summaryCardViewModel: SummaryCardViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        val recyclerView: RecyclerView = view.findViewById(R.id.summary_cards_recyclerview)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        val summaryCardAdapter = SummaryCardAdapter(emptyList(), requireContext())
        recyclerView.adapter = summaryCardAdapter
        summaryCardViewModel.allSummaryCard.observe(viewLifecycleOwner){
            summaryCardAdapter.updateSummaryCards(it)
        }

        summaryCardViewModel.loadAllSummaryCards()
        return view
    }

}