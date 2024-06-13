package it.unipd.dei.xml_frontend.ui.view.holder

import android.text.format.DateFormat
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import it.unipd.dei.common_backend.models.MovementWithCategory
import it.unipd.dei.common_backend.viewModels.CategoryViewModel
import it.unipd.dei.common_backend.viewModels.MovementWithCategoryViewModel
import it.unipd.dei.xml_frontend.R
import it.unipd.dei.xml_frontend.ui.bottomsheets.MovementBottomSheetFragment

class MovementViewHolder(
    private val itemView: View,
    private val parentFragmentManager: FragmentManager,
    private val movementWithCategoryViewModel: MovementWithCategoryViewModel,
    private val categoryViewModel: CategoryViewModel
) {
    private val categoryTextView: TextView = itemView.findViewById(R.id.movement_card_category)
    private val amountTextView: TextView = itemView.findViewById(R.id.movement_card_amount)
    private val dateTextView: TextView = itemView.findViewById(R.id.movement_card_date)
    private val imageView: ImageView = itemView.findViewById(R.id.movement_card_image)

    fun getItemView(): View {
        return itemView;
    }

    fun bind(
        movementWithCategory: MovementWithCategory
    ) {

        val amount = movementWithCategory.movement.amount
        amountTextView.text = amount.toString()
        categoryTextView.text = movementWithCategory.category.identifier
        dateTextView.text =
            DateFormat.format("dd/MM/yyyy hh:mm", movementWithCategory.movement.date)
                .toString()

        if (amount > 0) {
            imageView.setImageResource(R.drawable.baseline_trending_up_24)
            imageView.setBackgroundResource(R.drawable.circle_up)
        } else if (amount < 0) {
            imageView.setImageResource(R.drawable.baseline_trending_down_24)
            imageView.setBackgroundResource(R.drawable.circle_down)
        }

        itemView.setOnLongClickListener {
            MovementBottomSheetFragment(
                movementWithCategory,
                movementWithCategoryViewModel,
                categoryViewModel
            ).show(
                parentFragmentManager,
                "OptionCategoryModalBottomSheetFragment"
            )
            true
        }
    }
}