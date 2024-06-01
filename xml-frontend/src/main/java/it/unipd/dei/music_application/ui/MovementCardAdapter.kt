package it.unipd.dei.music_application.ui

import android.annotation.SuppressLint
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import it.unipd.dei.music_application.R
import it.unipd.dei.music_application.models.MovementWithCategory

class MovementCardAdapter(
    private val movements: LiveData<List<MovementWithCategory>>,
    private val longClickListener: OnItemLongClickListener,
    viewLifecycleOwner: LifecycleOwner,
) : RecyclerView.Adapter<MovementCardAdapter.MovementViewHolder>() {
    private var size: Int = movements.value?.size ?: 0

    init {
        movements.observe(viewLifecycleOwner) {
            notifyItemRangeInserted(size, itemCount)
            size = itemCount
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovementViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_movement_card, parent, false)
        return MovementViewHolder(view)
    }

    override fun onBindViewHolder(holder: MovementViewHolder, position: Int) {
        val movementWithCategory: MovementWithCategory = movements.value?.get(position) ?: return
        holder.bind(movementWithCategory, longClickListener)
    }

    override fun getItemCount(): Int = movements.value?.size ?: 0

    class MovementViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val categoryTextView: TextView = itemView.findViewById(R.id.movement_card_category)
        private val amountTextView: TextView = itemView.findViewById(R.id.movement_card_amount)
        private val dateTextView: TextView = itemView.findViewById(R.id.movement_card_date)
        private val imageView: ImageView = itemView.findViewById(R.id.movement_card_image)

        fun bind(movementWithCategory: MovementWithCategory, longClickListener: OnItemLongClickListener) {

            val amount = movementWithCategory.movement.amount
            amountTextView.text = amount.toString()
            categoryTextView.text = movementWithCategory.category.identifier
            dateTextView.text =
                DateFormat.format("dd/MM/yyyy hh:mm", movementWithCategory.movement.createdAt)
                    .toString()

            if (amount > 0) {
                imageView.setImageResource(R.drawable.baseline_trending_up_24)
                imageView.setBackgroundResource(R.drawable.circle_up)
            } else if (amount < 0) {
                imageView.setImageResource(R.drawable.baseline_trending_down_24)
                imageView.setBackgroundResource(R.drawable.circle_down)
            }

            itemView.setOnLongClickListener {
                longClickListener.onItemLongClick(adapterPosition)
                true
            }
        }
    }
}
