package it.unipd.dei.xml_frontend.ui.bottomsheets

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import it.unipd.dei.common_backend.models.MovementWithCategory
import it.unipd.dei.common_backend.view.CategoryViewModel
import it.unipd.dei.common_backend.view.MovementWithCategoryViewModel
import it.unipd.dei.xml_frontend.R
import it.unipd.dei.xml_frontend.ui.dialog.DeleteMovementDialog
import it.unipd.dei.xml_frontend.ui.dialog.EditMovementDialog
import it.unipd.dei.xml_frontend.ui.view.holder.MovementViewHolder

class MovementBottomSheetFragment(private val movement: MovementWithCategory) : BottomSheetDialogFragment(){
    private val categoryViewModel: CategoryViewModel by viewModels()
    private val movementWithCategoryViewModel: MovementWithCategoryViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(
            //TODO check if this is right
            R.layout.fragment_movement_bottom_sheet,
            container,
            false
        )
        val editMovementDialogView = inflater.inflate(
            R.layout.fragment_movement_dialog,
            container,
            false
        )
        val movementViewHolder = MovementViewHolder(
            view.findViewById(R.id.movement_card),
            parentFragmentManager
        )
        val showEditMovementDialogButtonView: View = view.findViewById(R.id.show_edit_movement_dialog_button)
        val showDeleteMovementDialogButtonView: View = view.findViewById(R.id.show_delete_movement_dialog_button)

        movementViewHolder.bind(movement)

        showEditMovementDialogButtonView.setOnClickListener {
            EditMovementDialog(
                movement,
                movementWithCategoryViewModel,
                categoryViewModel,
                editMovementDialogView,
                requireContext()
            ).show()
            dismiss()
        }

        showDeleteMovementDialogButtonView.setOnClickListener {
            DeleteMovementDialog(
                movementWithCategoryViewModel,
                requireContext(),
                movement.movement
            ).show()
            dismiss()
        }

        return view
    }
}