/*
TODO organizza il fragmnet in modo piu chiaro e ordinato:
    ├── ui/
    │   ├── fragment/
    │   │   ├── MyFragment.kt
    │   ├── viewmodel/
    │   │   ├── MyFragmentViewModel.kt
    │   ├── helper/
    │   │   ├── MyFragmentUIHelper.kt
    │   ├── extensions/
    │   │   ├── MyFragmentExtensions.kt
*/


package it.unipd.dei.music_application.ui.dialog

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.material.button.MaterialButtonToggleGroup
import com.google.android.material.textfield.TextInputLayout
import dagger.hilt.android.AndroidEntryPoint
import it.unipd.dei.music_application.R
import it.unipd.dei.music_application.models.Category
import it.unipd.dei.music_application.ui.dialog.helper.InputHelper
import it.unipd.dei.music_application.ui.dialog.helper.MovementInputHelper
import it.unipd.dei.music_application.utils.Constants.IDENTIFIER_ALREADY_PRESENT_ERROR_MESSAGE
import it.unipd.dei.music_application.utils.DisplayToast
import it.unipd.dei.music_application.view.CategoryViewModel
import it.unipd.dei.music_application.view.MovementWithCategoryViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.UUID

@AndroidEntryPoint
class DialogInputFragment : DialogFragment() {

    private val categoryViewModel: CategoryViewModel by viewModels()
    private val movementWithCategoryViewModel: MovementWithCategoryViewModel by viewModels()

    private lateinit var movementAmountTextField: EditText
    private lateinit var movementCategoryIdTextField: AutoCompleteTextView
    private lateinit var movementCategoryIdTextInputLayout: TextInputLayout
    private lateinit var movementCreatedAtTextField: EditText
    private lateinit var movementSubmitButton: Button
    private lateinit var sectionMovementLayout: ConstraintLayout

    private lateinit var categoryIdentifierTextField: EditText
    private lateinit var categorySubmitButton: Button
    private lateinit var sectionCategoryLayout: ConstraintLayout

    private lateinit var materialButtonToggleGroup: MaterialButtonToggleGroup

    private val _selectedOption = MutableLiveData<Int>()
    private val selectedOption: LiveData<Int> = _selectedOption

    private var selectedCategory: Category? = null
    private lateinit var categoryIdentifier: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeInputFields(view)
        initializeSections(view)
        initializeSubmitButton(view)
        initializeMaterialButtonToggleGroup(view)
        MovementInputHelper.setupMovementAmountTextField(movementAmountTextField)

        observeViewModels()

        categoryViewModel.getAllCategories()
    }

    private fun initializeInputFields(view: View) {
        movementAmountTextField = view.findViewById(R.id.input_movement_amount)
        movementCategoryIdTextField = view.findViewById(R.id.input_movement_category_id)
        movementCategoryIdTextField.apply {
            setOnItemClickListener { parent, _, position, _ ->
                clearFocus()
                selectedCategory = parent.getItemAtPosition(position) as Category
                setText(selectedCategory?.identifier)
            }
        }
        movementCategoryIdTextInputLayout =
            view.findViewById(R.id.input_movement_category_id_layout)
        movementCreatedAtTextField = view.findViewById(R.id.input_movement_created_at)
        movementCreatedAtTextField.setOnClickListener {
            InputHelper.selectDateTime(requireContext()) { selectedDateTime ->
                movementCreatedAtTextField.setText(selectedDateTime)
            }
        }
        categoryIdentifierTextField = view.findViewById(R.id.input_category_identifier)
    }

    private fun initializeSections(view: View) {
        sectionCategoryLayout = view.findViewById(R.id.section_category)
        sectionMovementLayout = view.findViewById(R.id.section_movement)
        _selectedOption.postValue(R.id.input_option_movement)
    }

    private fun initializeMaterialButtonToggleGroup(view: View) {
        materialButtonToggleGroup = view.findViewById(R.id.input_button_toggle_group)
        materialButtonToggleGroup.addOnButtonCheckedListener { _, checkedId, isChecked ->
            if (isChecked) _selectedOption.postValue(checkedId)
        }
    }

    private fun initializeSubmitButton(view: View) {
        movementSubmitButton = view.findViewById(R.id.input_movement_button)
        movementSubmitButton.text = requireContext().resources.getString(R.string.crea)
        movementSubmitButton.setOnClickListener {
            val movement = MovementInputHelper.handleMovementSubmission(
                selectedCategory,
                movementAmountTextField,
                movementSubmitButton,
                movementCategoryIdTextField,
                movementCreatedAtTextField
            )
            if (movement != null) {
                movementWithCategoryViewModel.upsertMovement(movement)
            }
        }

        categorySubmitButton = view.findViewById(R.id.input_category_button)
        categorySubmitButton.text = requireContext().resources.getString(R.string.crea)
        categorySubmitButton.setOnClickListener { handleCategorySubmission() }
    }

    private fun observeViewModels() {
        movementWithCategoryViewModel.upsertResult.observe(viewLifecycleOwner) {
            when (it) {
                true -> {
                    DisplayToast.displaySuccess(requireContext())
                    dismiss()
                }

                false -> DisplayToast.displayFailure(requireContext())
                null -> {}
            }
        }

        selectedOption.observe(viewLifecycleOwner) {
            changeInputFieldsById(it)
        }

        categoryViewModel.upsertResult.observe(viewLifecycleOwner) {
            when (it) {
                true -> {
                    DisplayToast.displaySuccess(requireContext())
                    dismiss()
                }

                false -> DisplayToast.displayFailure(requireContext())
                null -> {}
            }
        }

        categoryViewModel.allCategories.observe(viewLifecycleOwner) {
            MovementInputHelper.setAdapter(requireContext(),it,movementCategoryIdTextField)
        }

        categoryViewModel.isCategoryIdentifierPresent.observe(viewLifecycleOwner) {
            when (it) {
                true -> handleCategoryAlreadyPresent()
                false -> createAndInsertCategory()
                null -> {}
            }
        }
    }

    //TODO vedi se spostare queste 3 funzioni del CategoryInputHelper.kt
    private fun handleCategorySubmission() {
        categoryIdentifier = categoryIdentifierTextField.text.toString()
        if (categoryIdentifier.isEmpty()) {
            InputHelper.showTemporaryError(categoryIdentifierTextField, categorySubmitButton)
            return
        }
        // The access to database require an observer (isCategoryIdentifierPresent)
        categoryViewModel.isCategoryIdentifierPresent(categoryIdentifier)
    }

    private fun handleCategoryAlreadyPresent() {
        categoryIdentifierTextField.text = null
        val previousHint = categoryIdentifierTextField.hint
        categoryIdentifierTextField.hint = IDENTIFIER_ALREADY_PRESENT_ERROR_MESSAGE
        categorySubmitButton.isEnabled = false
        val previousButtonTextColor = categorySubmitButton.textColors
        categorySubmitButton.setTextColor(Color.RED)
        CoroutineScope(Dispatchers.Main).launch {
            delay(1500)
            categoryIdentifierTextField.hint = previousHint
            categoryIdentifierTextField.setText(categoryIdentifier)
            categorySubmitButton.setTextColor(previousButtonTextColor)
            categorySubmitButton.isEnabled = true
        }
    }

    private fun createAndInsertCategory() {
        val category = Category(
            UUID.randomUUID(),
            categoryIdentifier,
            System.currentTimeMillis(),
            System.currentTimeMillis()
        )
        categoryViewModel.upsertCategory(category)
    }



    private fun changeInputFieldsById(id: Int?) {
        when (id) {
            R.id.input_option_movement -> {
                sectionMovementLayout.visibility = View.VISIBLE
                sectionCategoryLayout.visibility = View.GONE
            }

            R.id.input_option_category -> {
                sectionMovementLayout.visibility = View.GONE
                sectionCategoryLayout.visibility = View.VISIBLE
            }
        }
    }
}
