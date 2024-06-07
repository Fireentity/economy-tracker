package it.unipd.dei.xml_frontend.ui.bottomsheets

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import it.unipd.dei.common_backend.models.Category
import it.unipd.dei.xml_frontend.ui.dialog.category.EditCategoryDialog
import it.unipd.dei.xml_frontend.ui.view.holder.MovementViewHolder
import it.unipd.dei.common_backend.view.CategoryViewModel
import it.unipd.dei.xml_frontend.R

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
        val view: View = inflater.inflate(R.layout.fragment_option_category, container, false)
        MovementViewHolder(view.findViewById(R.id.fragment_category_card), parentFragmentManager)

        val editLayout: View = view.findViewById(R.id.edit_layout)
        val deleteLayout: View = view.findViewById(R.id.delete_layout)

        editLayout.setOnClickListener {
            EditCategoryDialog(category,categoryViewModel,,requireContext()).show( )
            dismiss()
        }

        deleteLayout.setOnClickListener {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle(resources.getString(R.string.delete_category_title))
                .setMessage(resources.getString(R.string.delete_category_message))
                .setNeutralButton(resources.getString(R.string.cancel)) { _, _ -> }
                .setPositiveButton(resources.getString(R.string.si)) { _, _ -> categoryViewModel.deleteCategory(category) }
                .show()
        }

        return view
    }
}
