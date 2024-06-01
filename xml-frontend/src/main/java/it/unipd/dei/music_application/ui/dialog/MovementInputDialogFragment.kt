package it.unipd.dei.music_application.ui.dialog

import android.os.Bundle
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.google.android.material.textfield.TextInputLayout
import dagger.hilt.android.AndroidEntryPoint
import it.unipd.dei.music_application.R
import it.unipd.dei.music_application.models.Category
import it.unipd.dei.music_application.models.Movement
import it.unipd.dei.music_application.models.MovementWithCategory
import it.unipd.dei.music_application.ui.dialog.helper.InputHelper
import it.unipd.dei.music_application.ui.dialog.helper.MovementInputHelper
import it.unipd.dei.music_application.utils.DisplayToast
import it.unipd.dei.music_application.view.CategoryViewModel
import it.unipd.dei.music_application.view.MovementWithCategoryViewModel

@AndroidEntryPoint
class MovementInputDialogFragment(
    private val movementWithCategory: MovementWithCategory
) :
    DialogFragment() {

    private val categoryViewModel: CategoryViewModel by viewModels()
    private val movementWithCategoryViewModel: MovementWithCategoryViewModel by viewModels()

    private lateinit var amountTextField: EditText
    private lateinit var categoryIdTextField: AutoCompleteTextView
    private lateinit var categoryIdTextInputLayout: TextInputLayout
    private lateinit var createdAtTextField: EditText
    private lateinit var submitButton: Button

    private var selectedCategory: Category? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_input_movement, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeInputFields(view)
        movementWithCategoryViewModel.upsertResult.observe(viewLifecycleOwner) {
            when (it) {
                true -> {
                    DisplayToast.displaySuccess(requireContext())
                    dismiss()
                }

                false -> {
                    DisplayToast.displayFailure(requireContext())
                    dismiss()
                }

                null -> {}
            }
        }
        categoryViewModel.allCategories.observe(viewLifecycleOwner) {
            MovementInputHelper.setAdapter(
                requireContext(),
                it,
                categoryIdTextField
            )
        }
        categoryViewModel.getAllCategories()
        setParametersToInputFields()
    }

    private fun initializeInputFields(view: View) {
        amountTextField = view.findViewById(R.id.input_movement_amount)
        categoryIdTextField = view.findViewById(R.id.input_movement_category_id)
        categoryIdTextField.apply {
            setOnItemClickListener { parent, _, position, _ ->
                clearFocus()
                selectedCategory = parent.getItemAtPosition(position) as Category
                setText(selectedCategory?.identifier)
            }
        }
        categoryIdTextInputLayout =
            view.findViewById(R.id.input_movement_category_id_layout)
        createdAtTextField = view.findViewById(R.id.input_movement_created_at)
        createdAtTextField.setOnClickListener {
            InputHelper.selectDateTime(requireContext()) { selectedDateTime ->
                createdAtTextField.setText(selectedDateTime)
            }
        }

        submitButton = view.findViewById(R.id.input_movement_button)
        submitButton.setOnClickListener {
            val movement = MovementInputHelper.handleMovementSubmission(
                selectedCategory,
                amountTextField,
                submitButton,
                categoryIdTextField,
                createdAtTextField
            )
            if (movement != null) {
                val updatedMovement = Movement(
                    movementWithCategory.movement.uuid,
                    movement.amount,
                    movement.categoryId,
                    movement.updatedAt,
                    movement.createdAt
                )
                movementWithCategoryViewModel.upsertMovement(updatedMovement)
            }
        }
    }

    private fun setParametersToInputFields() {
        val amount = String.format("%.2f", movementWithCategory.movement.amount)
        amountTextField.setText(amount)

        createdAtTextField.setText(
            DateFormat.format("dd/MM/yyyy hh:mm", movementWithCategory.movement.createdAt)
                .toString()
        )


        categoryIdTextField.setText(movementWithCategory.category.identifier)
        selectedCategory = movementWithCategory.category
        categoryIdTextField.apply {
            setOnItemClickListener { parent, _, position, _ ->
                clearFocus()
                selectedCategory = parent.getItemAtPosition(position) as Category
                setText(selectedCategory?.identifier)
            }
        }
    }
}