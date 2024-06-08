package it.unipd.dei.xml_frontend.ui.bottomsheets

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import it.unipd.dei.common_backend.models.Category
import it.unipd.dei.common_backend.view.CategoryViewModel
import it.unipd.dei.xml_frontend.R
import it.unipd.dei.xml_frontend.ui.dialog.DeleteCategoryDialog
import it.unipd.dei.xml_frontend.ui.dialog.EditCategoryDialog
import it.unipd.dei.xml_frontend.ui.view.holder.CategoryViewHolder

@AndroidEntryPoint
class CategoryBottomSheetFragment(
    private val category: Category
) : BottomSheetDialogFragment() {

    private val categoryViewModel: CategoryViewModel by viewModels()

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
            R.layout.fragment_edit_category_dialog,
            container,
            false
        )
        val categoryViewHolder = CategoryViewHolder(
            view.findViewById(R.id.category_card),
            parentFragmentManager
        )
        val showEditCategoryDialogButtonView: View = view.findViewById(R.id.show_edit_category_dialog_button)
        val deleteLayout: View = view.findViewById(R.id.show_delete_category_dialog_button)

        categoryViewHolder.bind(category)

        showEditCategoryDialogButtonView.setOnClickListener {
            EditCategoryDialog(
                category,
                categoryViewModel,
                editCategoryDialogView,
                requireContext()
            ).show()
            dismiss()
        }

        deleteLayout.setOnClickListener {
            DeleteCategoryDialog(
                categoryViewModel,
                requireContext(),
                category
            ).show()
            dismiss()
        }

        return view
    }
}
