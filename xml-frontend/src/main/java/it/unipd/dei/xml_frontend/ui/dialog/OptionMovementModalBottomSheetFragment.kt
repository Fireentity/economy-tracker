package it.unipd.dei.music_application.ui.dialog

import android.os.Bundle
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import it.unipd.dei.music_application.R
import it.unipd.dei.common_backend.models.MovementWithCategory
import it.unipd.dei.common_backend.utils.DisplayToast
import it.unipd.dei.common_backend.view.MovementWithCategoryViewModel
import it.unipd.dei.xml_frontend.ui.dialog.MovementInputDialogFragment

@AndroidEntryPoint
class OptionMovementModalBottomSheetFragment(
    private val movementWithCategory: MovementWithCategory
) : BottomSheetDialogFragment() {

    private val movementWithCategoryViewModel: MovementWithCategoryViewModel by viewModels()

    private lateinit var movementImageView: ImageView
    private lateinit var movementCategoryIdTextView: TextView
    private lateinit var movementAmountTextView: TextView
    private lateinit var movementDateTextView: TextView

    private lateinit var editLayout: LinearLayout
    private lateinit var deleteLayout: LinearLayout

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_option_movement, container, false)

        initializeViews(view)
        setupMovementUI()


        setupListeners()
        observeDeleteResult()
        return view
    }

    private fun setupListeners() {
        editLayout.setOnClickListener {
            showEditDialog()
        }

        deleteLayout.setOnClickListener {
            showDeleteConfirmationDialog()
        }
    }

    private fun showEditDialog() {
        val title = context?.resources?.getString(R.string.edit_movement_title)
        val buttonText = context?.resources?.getString(R.string.edit_movement_button)
        val movementInputDialogFragment =
            MovementInputDialogFragment(title, buttonText, movementWithCategory)
        movementInputDialogFragment.show(parentFragmentManager, "MovementInputDialogFragment")

        dismiss()
    }

    private fun showDeleteConfirmationDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(resources.getString(R.string.delete_movement_title))
            .setMessage(resources.getString(R.string.delete_movement_message))
            .setNeutralButton(resources.getString(R.string.cancel)) { _, _ -> }
            .setPositiveButton(resources.getString(R.string.si)) { _, _ -> deleteMovement() }
            .show()
    }

    private fun observeDeleteResult() {
        movementWithCategoryViewModel.deleteResult.observe(viewLifecycleOwner) {
            when (it) {
                true -> {
                    DisplayToast.displaySuccess(requireContext())
                    dismiss()
                }

                false -> DisplayToast.displayFailure(requireContext())
                null -> {}
            }
        }
    }

    private fun deleteMovement() {

        movementWithCategoryViewModel.deleteMovement(movementWithCategory.movement)

    }

    private fun initializeViews(view: View) {

        editLayout = view.findViewById(R.id.edit_layout)
        deleteLayout = view.findViewById(R.id.delete_layout)


        movementImageView = view.findViewById(R.id.movement_card_image)
        movementCategoryIdTextView = view.findViewById(R.id.movement_card_category)
        movementAmountTextView = view.findViewById(R.id.movement_card_amount)
        movementDateTextView = view.findViewById(R.id.movement_card_date)
    }

    //TODO sposta in helper e riusa anche in MovementCardAdapter
    private fun setupMovementImageView(amount: Double) {
        if (amount > 0) {
            movementImageView.setImageResource(R.drawable.baseline_trending_up_24)
            movementImageView.setBackgroundResource(R.drawable.circle_up)
        } else if (amount < 0) {
            movementImageView.setImageResource(R.drawable.baseline_trending_down_24)
            movementImageView.setBackgroundResource(R.drawable.circle_down)
        }
    }

    private fun setupMovementUI() {

        val amount = movementWithCategory.movement.amount
        movementAmountTextView.text = String.format("%.2f", amount)
        movementCategoryIdTextView.text = movementWithCategory.category.identifier
        movementDateTextView.text =
            DateFormat.format("dd/MM/yyyy hh:mm", movementWithCategory.movement.createdAt)
                .toString()
        setupMovementImageView(amount)

    }

}
