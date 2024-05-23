package it.unipd.dei.music_application.ui

import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import it.unipd.dei.music_application.R
import it.unipd.dei.music_application.models.MovementWithCategory

class MovementCardAdapter(private var movements: List<MovementWithCategory>) :
    RecyclerView.Adapter<MovementCardAdapter.MovementViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovementViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_movement_card, parent, false)
        return MovementViewHolder(view)
    }

    override fun onBindViewHolder(holder: MovementViewHolder, position: Int) {
        val movementWithCategory = movements[position]
        holder.bind(movementWithCategory)
    }

    override fun getItemCount(): Int = movements.size
    // Metodo per aggiornare i movimenti e notificare i cambiamenti

    fun updateMovements(newMovements: List<MovementWithCategory>) {
        this.movements = newMovements
        notifyDataSetChanged() // Notifica che i dati sono cambiati
    }

    fun updateMovements(
        newMovements: List<MovementWithCategory>,
        startChangePosition: Int,
        itemCount: Int
    ) {
        this.movements = newMovements
        notifyItemRangeInserted(startChangePosition, itemCount) // Notifica che i dati sono cambiati
    }

    fun getMovementsCount(): Int {
        return movements.size
    }

    class MovementViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        //private val nameTextView: TextView = itemView.findViewById(R.id.image)
        private val categoryTextView: TextView = itemView.findViewById(R.id.movement_card_category)
        private val amountTextView: TextView = itemView.findViewById(R.id.movement_card_amount)
        private val dateTextView: TextView = itemView.findViewById(R.id.movement_card_date)
        private val imageView: ImageView = itemView.findViewById(R.id.movement_card_image)
        fun bind(movementWithCategory: MovementWithCategory) {

            val amount = movementWithCategory.movement.amount
            //Setto la quantità che va formattata come number.##
            amountTextView.text = String.format("%.2f", amount)
            //Setto la categoria
            categoryTextView.text = movementWithCategory.category.identifier
            //Setto la data
            dateTextView.text =
                DateFormat.format("dd/MM/yyyy hh:mm", movementWithCategory.movement.createdAt)
                    .toString()

            //metto l'immagine corretta (freccia in su sfondo verde, freccia giu sfondo rosso)
            if (amount > 0) {
                imageView.setImageResource(R.drawable.baseline_trending_up_24)
                imageView.setBackgroundResource(R.drawable.circle_up)
            } else if (amount < 0) {
                imageView.setImageResource(R.drawable.baseline_trending_down_24)
                imageView.setBackgroundResource(R.drawable.circle_down)
            }
        }
    }
}