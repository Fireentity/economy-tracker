package it.unipd.dei.xml_frontend.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import it.unipd.dei.common_backend.models.Summary
import it.unipd.dei.xml_frontend.R
import it.unipd.dei.xml_frontend.ui.view.holder.SummaryViewHolder

class SummaryCardAdapter(private var summaries: List<Summary>, private val context: Context) :
    RecyclerView.Adapter<SummaryCardAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_summary_card, parent, false)
        return ViewHolder(SummaryViewHolder(view, context))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val summary: Summary = summaries[position]
        holder.bind(summary)
    }

    fun updateSummaryCards(summaries: List<Summary>){
        this.summaries = summaries
        //TODO check here
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = summaries.size

    class ViewHolder(private val viewHolder: SummaryViewHolder) :
        RecyclerView.ViewHolder(viewHolder.getItemView()) {

        fun bind(summary: Summary) {
            viewHolder.bind(summary)
        }
    }
}
