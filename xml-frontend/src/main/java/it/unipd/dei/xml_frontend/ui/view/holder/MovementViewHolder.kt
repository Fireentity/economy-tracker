package it.unipd.dei.xml_frontend.ui.view.holder

import android.content.Context
import android.content.res.ColorStateList
import android.text.format.DateFormat
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import it.unipd.dei.common_backend.models.MovementWithCategory
import it.unipd.dei.xml_frontend.R
import it.unipd.dei.xml_frontend.ui.bottomsheets.MovementBottomSheetFragment

class MovementViewHolder(
    private val itemView: View,
    private val parentFragmentManager: FragmentManager
)  {
    private val categoryTextView: TextView = itemView.findViewById(R.id.movement_card_category)
    private val amountTextView: TextView = itemView.findViewById(R.id.movement_card_amount)
    private val dateTextView: TextView = itemView.findViewById(R.id.movement_card_date)
    private val imageView: ImageView = itemView.findViewById(R.id.movement_card_image)

    fun getItemView(): View {
        return itemView;
    }

    fun bind(
        movementWithCategory: MovementWithCategory,
        context: Context

    ) {

        val amount = movementWithCategory.movement.amount
        amountTextView.text = amount.toString()
        categoryTextView.text = movementWithCategory.category.identifier
        //TODO check here
        dateTextView.text =
            DateFormat.format("dd/MM/yyyy hh:mm", movementWithCategory.movement.date)
                .toString()

        if (amount > 0) {
            imageView.setImageResource(R.drawable.baseline_trending_up_24)
            imageView.backgroundTintList = ColorStateList.valueOf(context.getColor(R.color.green_100))
            imageView.imageTintList = ColorStateList.valueOf(context.getColor(R.color.green_700))
        } else if (amount < 0) {
            imageView.setImageResource(R.drawable.baseline_trending_down_24)
            imageView.backgroundTintList = ColorStateList.valueOf(context.getColor(R.color.red_100))
            imageView.imageTintList = ColorStateList.valueOf(context.getColor(R.color.red_700))
        }

        itemView.setOnLongClickListener {
            MovementBottomSheetFragment(
                movementWithCategory
            ).show(
                parentFragmentManager,
                "OptionCategoryModalBottomSheetFragment"
            )
            true
        }
    }
}