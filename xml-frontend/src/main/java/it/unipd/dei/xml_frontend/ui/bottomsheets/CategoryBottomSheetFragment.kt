package it.unipd.dei.xml_frontend.ui.bottomsheets

import android.content.Context.MODE_PRIVATE
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.activityViewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import it.unipd.dei.common_backend.models.Category
import it.unipd.dei.common_backend.viewModels.CategoryViewModel
import it.unipd.dei.common_backend.viewModels.MovementWithCategoryViewModel
import it.unipd.dei.common_backend.viewModels.SummaryViewModel
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
    private val summaryViewModel: SummaryViewModel by activityViewModels()

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
            movementWithCategoryViewModel,
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
            summaryViewModel,
            requireContext(),
            category
        )
        deleteLayout.setOnClickListener {
            deleteCategoryDialog.show()
            dismiss()
        }

        val sharedPref = requireActivity().getPreferences(MODE_PRIVATE)

        val isDarkModeEnable = sharedPref.getBoolean(requireContext().getString(R.string.is_dark_mode_enable), false)
        if(isDarkModeEnable){
            val editIcon: ImageView = view.findViewById(R.id.edit_category_icon)
            val deleteIcon: ImageView = view.findViewById(R.id.delete_category_icon)
            editIcon.imageTintList = ColorStateList.valueOf(requireContext().getColor(R.color.gray_100))
            deleteIcon.imageTintList = ColorStateList.valueOf(requireContext().getColor(R.color.gray_100))
        }

        return view
    }
}
