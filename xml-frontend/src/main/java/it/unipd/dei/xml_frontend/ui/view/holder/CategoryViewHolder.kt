package it.unipd.dei.xml_frontend.ui.view.holder

import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import it.unipd.dei.common_backend.models.Category
import it.unipd.dei.common_backend.view.CategoryViewModel
import it.unipd.dei.common_backend.view.MovementWithCategoryViewModel
import it.unipd.dei.xml_frontend.R
import it.unipd.dei.xml_frontend.ui.bottomsheets.CategoryBottomSheetFragment
import java.util.PrimitiveIterator

class CategoryViewHolder(
    private val itemView: View,
    private val parentFragmentManager: FragmentManager,
) {
    private val categoryIdentifier: TextView = itemView.findViewById(R.id.category_card_identifier)

    fun getItemView(): View {
        return itemView;
    }

    fun bindWithButton(
        category: Category,
        categoryViewModel: CategoryViewModel,
    ) {
        val categoryImageButton: ImageButton = itemView.findViewById(R.id.view_category_button)
        categoryIdentifier.text = category.identifier
        categoryImageButton.setOnClickListener {
            CategoryBottomSheetFragment(
                category,
                categoryViewModel
            ).show(
                parentFragmentManager,
                "OptionCategoryModalBottomSheetFragment"
            )
        }
    }

    fun bindWithoutButton(category: Category) {
        categoryIdentifier.text = category.identifier
    }

}