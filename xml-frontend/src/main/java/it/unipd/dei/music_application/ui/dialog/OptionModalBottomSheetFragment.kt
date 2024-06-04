package it.unipd.dei.music_application.ui.dialog

import android.os.Bundle
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.divider.MaterialDivider
import dagger.hilt.android.AndroidEntryPoint
import it.unipd.dei.music_application.R
import it.unipd.dei.music_application.models.Category
import it.unipd.dei.music_application.models.MovementWithCategory
import it.unipd.dei.music_application.utils.DisplayToast
import it.unipd.dei.music_application.view.CategoryViewModel
import it.unipd.dei.music_application.view.MovementWithCategoryViewModel

@AndroidEntryPoint
class OptionModalBottomSheetFragment(
    private val movementWithCategory: MovementWithCategory?,
    private val category: Category?
) : BottomSheetDialogFragment() {

    private val categoryViewModel: CategoryViewModel by viewModels()
    private val movementWithCategoryViewModel: MovementWithCategoryViewModel by viewModels()

    private lateinit var categoryCard: View
    private lateinit var categoryIdentifier: TextView
    private lateinit var categoryImageButton: ImageButton
    private lateinit var categoryCardDivider: MaterialDivider

    private lateinit var movementCard: View
    private lateinit var movementImageView: ImageView
    private lateinit var movementCategoryIdTextView: TextView
    private lateinit var movementAmountTextView: TextView
    private lateinit var movementDateTextView: TextView
    private lateinit var movementCardDivider: MaterialDivider

    private lateinit var editLayout: LinearLayout
    private lateinit var deleteLayout: LinearLayout

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_option, container, false)

        if (!validateInputs()) return view

        initializeViews(view)
        if (movementWithCategory != null) setupMovementUI()
        if (category != null) setupCategoryUI()

        setupListeners()
        observeDeleteResult()
        return view
    }

    private fun validateInputs(): Boolean {
        return (category != null && movementWithCategory == null) || (category == null && movementWithCategory != null)
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

        movementWithCategory?.let {
            val title = context?.resources?.getString(R.string.edit_movement_title)
            val buttonText = context?.resources?.getString(R.string.edit_movement_button)
            val movementInputDialogFragment =
                MovementInputDialogFragment(title, buttonText, movementWithCategory)
            movementInputDialogFragment.show(parentFragmentManager, "MovementInputDialogFragment")
        }
        category?.let {
            val title = context?.resources?.getString(R.string.edit_category_title)
            val buttonText = context?.resources?.getString(R.string.edit_category_button)
            val categoryInputDialogFragment =
                CategoryInputDialogFragment(title, buttonText, category)
            categoryInputDialogFragment.show(parentFragmentManager, "CategoryInputDialogFragment")

        }
        dismiss()
    }

    private fun showDeleteConfirmationDialog() {
        if (movementWithCategory != null) {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle(resources.getString(R.string.delete_movement_title))
                .setMessage(resources.getString(R.string.delete_movement_message))
                .setNeutralButton(resources.getString(R.string.cancel)) { _, _ -> }
                .setPositiveButton(resources.getString(R.string.si)) { _, _ -> deleteMovement() }
                .show()
        } else if (category != null) {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle(resources.getString(R.string.delete_category_title))
                .setMessage(resources.getString(R.string.delete_category_message))
                .setNeutralButton(resources.getString(R.string.cancel)) { _, _ -> }
                .setPositiveButton(resources.getString(R.string.si)) { _, _ -> deleteCategory() }
                .show()
        }
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
        categoryViewModel.deleteResult.observe(viewLifecycleOwner) {
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
        movementWithCategory?.let {
            movementWithCategoryViewModel.deleteMovement(it.movement)
        }
    }

    private fun deleteCategory() {
        category?.let {
            categoryViewModel.deleteCategory(category)
        }
    }

    private fun initializeViews(view: View) {
        categoryCard = view.findViewById(R.id.category_card)
        movementCard = view.findViewById(R.id.movement_card)

        editLayout = view.findViewById(R.id.edit_layout)
        deleteLayout = view.findViewById(R.id.delete_layout)

        movementWithCategory?.let {
            movementImageView = view.findViewById(R.id.movement_card_image)
            movementCategoryIdTextView = view.findViewById(R.id.movement_card_category)
            movementAmountTextView = view.findViewById(R.id.movement_card_amount)
            movementDateTextView = view.findViewById(R.id.movement_card_date)
            movementCardDivider = view.findViewById(R.id.movement_card_divider)
        }

        category?.let {
            categoryIdentifier = view.findViewById(R.id.category_card_identifier)
            categoryImageButton = view.findViewById(R.id.view_category_button)
            categoryCardDivider = view.findViewById(R.id.category_card_divider)
        }
    }

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
        movementCard.visibility = View.VISIBLE
        movementCardDivider.visibility = View.GONE

        movementWithCategory?.let {
            val amount = it.movement.amount
            movementAmountTextView.text = String.format("%.2f", amount)
            movementCategoryIdTextView.text = it.category.identifier
            movementDateTextView.text =
                DateFormat.format("dd/MM/yyyy hh:mm", it.movement.createdAt).toString()
            setupMovementImageView(amount)
        }
    }

    private fun setupCategoryUI() {
        categoryCard.visibility = View.VISIBLE
        categoryImageButton.visibility = View.GONE
        categoryCardDivider.visibility = View.GONE
        category?.let {
            categoryIdentifier.text = it.identifier
        }
    }
}
