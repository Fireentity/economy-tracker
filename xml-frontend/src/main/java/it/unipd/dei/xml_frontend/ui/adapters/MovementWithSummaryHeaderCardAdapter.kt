package it.unipd.dei.xml_frontend.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import it.unipd.dei.common_backend.models.MovementWithCategory
import it.unipd.dei.common_backend.models.Summary
import it.unipd.dei.xml_frontend.R
import it.unipd.dei.xml_frontend.ui.view.holder.MovementViewHolder
import it.unipd.dei.xml_frontend.ui.view.holder.SummaryViewHolder

class MovementWithSummaryHeaderCardAdapter(
    private var summary: Summary,
    private var movements: List<MovementWithCategory>,
    private val parentFragmentManager: FragmentManager,
    private val context: Context
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    //TODO check here
    private val _header = 0
    private val _item = 1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return if (viewType == _header) {
            val view = LayoutInflater.from(parent.context).inflate(
                R.layout.fragment_summary_card,
                parent,
                false
            )
            HeaderViewHolder(SummaryViewHolder(view, context))
        } else {
            val view = LayoutInflater.from(parent.context).inflate(
                R.layout.fragment_movement_card,
                parent,
                false
            )
            ItemViewHolder(MovementViewHolder(view, parentFragmentManager))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is HeaderViewHolder) {
            holder.bind(summary)
        } else if (holder is ItemViewHolder) {
            val movementWithCategory: MovementWithCategory = movements[position - 1] //0 is header
            holder.bind(movementWithCategory, context)
        }
    }

    fun updateMovements(movements: List<MovementWithCategory>){
        this.movements = movements
        //TODO check here
        notifyDataSetChanged()
    }

    fun updateSummary(summary: Summary){
        this.summary = summary
        notifyItemChanged(0)
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) _header else _item
    }

    override fun getItemCount(): Int = movements.size + 1

    inner class ItemViewHolder(private val viewHolder: MovementViewHolder) : RecyclerView.ViewHolder(viewHolder.getItemView()) {

        fun bind(movementWithCategory: MovementWithCategory, context: Context) {
            viewHolder.bind(movementWithCategory, context)
        }
    }
    inner class HeaderViewHolder(private val viewHolder: SummaryViewHolder) : RecyclerView.ViewHolder(viewHolder.getItemView()) {
        fun bind(summary: Summary) {
            viewHolder.bind(summary)
        }
    }
}
