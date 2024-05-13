package it.unipd.dei.music_application.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import dagger.hilt.android.AndroidEntryPoint
import it.unipd.dei.music_application.R
import it.unipd.dei.music_application.database.BalanceDatabase
import javax.inject.Inject

class RegisterFragment : Fragment() {
    @Inject
    lateinit var database: BalanceDatabase

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val view = inflater.inflate(R.layout.fragment_register, container, false)
        val chart = view.findViewById<PieChart>(R.id.pie_chart);
        val movements = database.getMovementDao().getAllMovements();
        val recycler = view.findViewById<RecyclerView>(R.id.movements_recycler_view);

        val visitors: ArrayList<PieEntry> = arrayListOf(
            PieEntry(500F,"value2")
        )
        chart.data = PieData(PieDataSet(visitors, "test"))

        return view;
    }
}