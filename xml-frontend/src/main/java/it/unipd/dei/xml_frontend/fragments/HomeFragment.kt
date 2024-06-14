package it.unipd.dei.xml_frontend.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import it.unipd.dei.common_backend.viewModels.SummaryViewModel
import it.unipd.dei.xml_frontend.R
import it.unipd.dei.xml_frontend.ui.adapters.SummaryCardAdapter
import it.unipd.dei.xml_frontend.ui.buttons.ShowSettingsDialogButton

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private val summaryViewModel: SummaryViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        val recyclerView: RecyclerView = view.findViewById(R.id.summary_cards_recyclerview)
        val summaryCardAdapter = SummaryCardAdapter(emptyList(), requireContext())
        recyclerView.adapter = summaryCardAdapter
        summaryViewModel.allSummary.observe(viewLifecycleOwner){
            summaryCardAdapter.updateSummaryCards(it)
        }

        val settingsDialogView = inflater.inflate(R.layout.fragment_settings_dialog, container, false)

        val showSettingsDialogButton = ShowSettingsDialogButton(
            requireActivity(),
            requireContext(),
            settingsDialogView
        )
        val settingsIcon = view.findViewById<View>(R.id.settings_icon_button)
        settingsIcon.setOnClickListener {
            showSettingsDialogButton.onClick()
        }

        return view
    }

    override fun onResume() {
        super.onResume()
        summaryViewModel.loadAllSummaries()
    }
}