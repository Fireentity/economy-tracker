package it.unipd.dei.xml_frontend.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import it.unipd.dei.common_backend.models.MovementWithCategory
import it.unipd.dei.common_backend.viewModels.CategoryViewModel
import it.unipd.dei.common_backend.viewModels.MovementWithCategoryViewModel
import it.unipd.dei.xml_frontend.R
import it.unipd.dei.xml_frontend.ui.view.holder.MovementViewHolder

class MovementCardAdapter(
    private var movements: List<MovementWithCategory>,
    private val parentFragmentManager: FragmentManager,
    private val movementWithCategoryViewModel: MovementWithCategoryViewModel,
    private val categoryViewModel: CategoryViewModel
) : RecyclerView.Adapter<MovementCardAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_movement_card_with_divider, parent, false)
        return ViewHolder(MovementViewHolder(view, parentFragmentManager, movementWithCategoryViewModel, categoryViewModel))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val movementWithCategory: MovementWithCategory = movements[position]
        holder.bind(movementWithCategory)
    }

    //TODO crea la funzione ottimizzata
    fun updateMovements(movements: List<MovementWithCategory>){
        this.movements = movements
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = movements.size

    class ViewHolder(private val viewHolder: MovementViewHolder) : RecyclerView.ViewHolder(viewHolder.getItemView()) {

        fun bind(movementWithCategory: MovementWithCategory) {
            viewHolder.bind(movementWithCategory)
        }
    }
}
