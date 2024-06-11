package it.unipd.dei.xml_frontend.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import it.unipd.dei.common_backend.models.SummaryCard
import it.unipd.dei.xml_frontend.R
import it.unipd.dei.xml_frontend.ui.view.holder.SummaryViewHolder

class SummaryCardAdapter(private var summaryCards: List<SummaryCard>, private val context: Context) :
    RecyclerView.Adapter<SummaryCardAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SummaryCardAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_summary_card, parent, false)
        return SummaryCardAdapter.ViewHolder(SummaryViewHolder(view, context))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val summaryCard: SummaryCard = summaryCards[position]
        holder.bind(summaryCard)
    }

    fun updateSummaryCards(summaryCards: List<SummaryCard>){
        this.summaryCards = summaryCards
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = summaryCards.size

    class ViewHolder(private val viewHolder: SummaryViewHolder) :
        RecyclerView.ViewHolder(viewHolder.getItemView()) {

        fun bind(summaryCard: SummaryCard) {
            viewHolder.bind(summaryCard)
        }
    }
}
