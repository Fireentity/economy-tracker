package it.unipd.dei.xml_frontend.ui.bottomsheets

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import it.unipd.dei.common_backend.models.Category
import it.unipd.dei.common_backend.viewModels.CategoryViewModel
import it.unipd.dei.common_backend.viewModels.MovementWithCategoryViewModel
import it.unipd.dei.xml_frontend.R
import it.unipd.dei.xml_frontend.ui.dialog.DeleteCategoryDialog
import it.unipd.dei.xml_frontend.ui.dialog.UpsertCategoryDialog
import it.unipd.dei.xml_frontend.ui.view.holder.CategoryViewHolder

@AndroidEntryPoint
class CategoryBottomSheetFragment(
    private val category: Category,
) : BottomSheetDialogFragment() {

    private val movementWithCategoryViewModel: MovementWithCategoryViewModel by activityViewModels()
    private val categoryViewModel: CategoryViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(
            R.layout.fragment_category_bottom_sheet,
            container,
            false
        )
        val editCategoryDialogView = inflater.inflate(
            R.layout.fragment_category_dialog,
            container,
            false
        )
        val categoryViewHolder = CategoryViewHolder(
            view.findViewById(R.id.category_card),
            parentFragmentManager
        )
        val showEditCategoryDialogButtonView: View =
            view.findViewById(R.id.show_edit_category_dialog_button)
        val deleteLayout: View = view.findViewById(R.id.show_delete_category_dialog_button)

        categoryViewHolder.bindWithoutButton(category)

        val upsertCategoryDialog = UpsertCategoryDialog(
            categoryViewModel,
            editCategoryDialogView,
            requireContext(),
            getString(R.string.edit_category),
            category,
        )
        showEditCategoryDialogButtonView.setOnClickListener {
            upsertCategoryDialog.show()
            dismiss()
        }
        val deleteCategoryDialog = DeleteCategoryDialog(
            categoryViewModel,
            movementWithCategoryViewModel,
            requireContext(),
            category
        )
        deleteLayout.setOnClickListener {
            deleteCategoryDialog.show()
            dismiss()
        }

        return view
    }
}
