package it.unipd.dei.xml_frontend.ui.bottomsheets

import android.content.Context.MODE_PRIVATE
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.activityViewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import it.unipd.dei.common_backend.models.MovementWithCategory
import it.unipd.dei.common_backend.viewModels.CategoryViewModel
import it.unipd.dei.common_backend.viewModels.MovementWithCategoryViewModel
import it.unipd.dei.common_backend.viewModels.SummaryViewModel
import it.unipd.dei.xml_frontend.R
import it.unipd.dei.xml_frontend.ui.dialog.DeleteMovementDialog
import it.unipd.dei.xml_frontend.ui.dialog.UpsertMovementDialog
import it.unipd.dei.xml_frontend.ui.view.holder.MovementViewHolder

@AndroidEntryPoint
class MovementBottomSheetFragment(
    private val movement: MovementWithCategory,
) : BottomSheetDialogFragment() {

    private val movementWithCategoryViewModel: MovementWithCategoryViewModel by activityViewModels()
    private val categoryViewModel: CategoryViewModel by activityViewModels()
    private val summaryViewModel: SummaryViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(
            R.layout.fragment_movement_bottom_sheet, container, false
        )
        val editMovementDialogView = inflater.inflate(
            R.layout.fragment_movement_dialog, container, false
        )
        val movementViewHolder = MovementViewHolder(
            view.findViewById(R.id.movement_card),
            parentFragmentManager
        )
        val showEditMovementDialogButtonView: View =
            view.findViewById(R.id.show_edit_movement_dialog_button)
        val showDeleteMovementDialogButtonView: View =
            view.findViewById(R.id.show_delete_movement_dialog_button)

        movementViewHolder.bind(movement, requireContext())

        showEditMovementDialogButtonView.setOnClickListener {
            UpsertMovementDialog(
                categoryViewModel,
                movementWithCategoryViewModel,
                summaryViewModel,
                editMovementDialogView,
                requireContext(),
                viewLifecycleOwner,
                requireContext().getString(R.string.edit_movement),
                parentFragmentManager,
                movement
            ).show()
            dismiss()
        }

        showDeleteMovementDialogButtonView.setOnClickListener {
            DeleteMovementDialog(
                movementWithCategoryViewModel,
                summaryViewModel,
                requireContext(),
                movement
            ).show()
            dismiss()
        }


        val sharedPref = requireActivity().getPreferences(MODE_PRIVATE)

        val isDarkModeEnable = sharedPref.getBoolean(requireContext().getString(R.string.is_dark_mode_enable), false)
        if(isDarkModeEnable){
            val editIcon: ImageView = view.findViewById(R.id.edit_movement_icon)
            val deleteIcon: ImageView = view.findViewById(R.id.delete_movement_icon)
            editIcon.imageTintList = ColorStateList.valueOf(requireContext().getColor(R.color.gray_100))
            deleteIcon.imageTintList = ColorStateList.valueOf(requireContext().getColor(R.color.gray_100))
        }

        return view
    }
}