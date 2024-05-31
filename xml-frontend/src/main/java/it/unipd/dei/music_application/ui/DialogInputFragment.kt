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


package it.unipd.dei.music_application.ui

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.graphics.Color
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.material.button.MaterialButtonToggleGroup
import com.google.android.material.textfield.TextInputLayout
import dagger.hilt.android.AndroidEntryPoint
import it.unipd.dei.music_application.R
import it.unipd.dei.music_application.models.Category
import it.unipd.dei.music_application.models.Movement
import it.unipd.dei.music_application.utils.Constants.IDENTIFIER_ALREADY_PRESENT_ERROR_MESSAGE
import it.unipd.dei.music_application.utils.DisplayToast.Companion.displayFailure
import it.unipd.dei.music_application.utils.DisplayToast.Companion.displaySuccess
import it.unipd.dei.music_application.view.CategoryViewModel
import it.unipd.dei.music_application.view.MovementWithCategoryViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Locale
import java.util.UUID

@AndroidEntryPoint
class DialogInputFragment : DialogFragment(){

    private val categoryViewModel: CategoryViewModel by viewModels()
    private val movementWithCategoryViewModel: MovementWithCategoryViewModel by viewModels()

    private lateinit var movementAmountTextField: EditText
    private lateinit var movementCategoryIdTextField: AutoCompleteTextView
    private lateinit var movementCategoryIdTextInputLayout: TextInputLayout
    private lateinit var movementCreatedAtTextField: EditText
    private lateinit var categoryIdentifierTextField: EditText
    private lateinit var sectionMovementLinearLayout: LinearLayout
    private lateinit var sectionCategoryLinearLayout: LinearLayout
    private lateinit var submitButton: Button
    private lateinit var materialButtonToggleGroup: MaterialButtonToggleGroup

    private val _selectedOption = MutableLiveData<Int>()
    private val selectedOption: LiveData<Int> = _selectedOption

    private lateinit var selectedCategory: Category
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
        setupMovementAmountTextField()

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
                setText(selectedCategory.identifier)
            }
        }
        movementCategoryIdTextInputLayout =
            view.findViewById(R.id.input_movement_category_id_layout)
        movementCreatedAtTextField = view.findViewById(R.id.input_movement_created_at)
        movementCreatedAtTextField.setOnClickListener { showDateTimePickerDialog() }
        categoryIdentifierTextField = view.findViewById(R.id.input_category_identifier)
    }

    private fun initializeSections(view: View) {
        sectionCategoryLinearLayout = view.findViewById(R.id.section_category)
        sectionMovementLinearLayout = view.findViewById(R.id.section_movement)
        _selectedOption.postValue(R.id.input_option_movement)
    }

    private fun initializeMaterialButtonToggleGroup(view: View) {
        materialButtonToggleGroup = view.findViewById(R.id.input_button_toggle_group)
        materialButtonToggleGroup.addOnButtonCheckedListener { _, checkedId, isChecked ->
            if (isChecked) _selectedOption.postValue(checkedId)
        }
    }

    private fun initializeSubmitButton(view: View) {
        submitButton = view.findViewById(R.id.input_submit_button)
        submitButton.setOnClickListener { handleSubmit() }
    }

    private fun setupMovementAmountTextField() {
        val decimalDigitsInputFilter = InputFilter { source, _, _, dest, _, _ ->
            val inputText = dest.toString() + source.toString()
            if (inputText.matches(Regex("^(-)?\\d{0,6}(\\.\\d{0,2})?$"))) null else ""
        }
        movementAmountTextField.filters = arrayOf(decimalDigitsInputFilter)

        movementAmountTextField.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(editable: Editable?) {
                val text = editable.toString()
                if (text.isNotEmpty() && text != "-" && !text.endsWith(".")) {
                    val decimalIndex = text.indexOf(".")
                    if (decimalIndex != -1 && text.length - decimalIndex > 3) {
                        movementAmountTextField.setText(text.substring(0, decimalIndex + 3))
                        movementAmountTextField.setSelection(text.length - 1)
                    }
                }
            }
        })
    }

    private fun observeViewModels() {
        movementWithCategoryViewModel.insertResult.observe(viewLifecycleOwner) {
            when (it) {
                true -> {
                    displaySuccess(requireContext())
                    dismiss()
                }
                false -> displayFailure(requireContext())
                null -> {}
            }
        }

        selectedOption.observe(viewLifecycleOwner) {
            changeInputFieldsById(it)
        }

        categoryViewModel.insertResult.observe(viewLifecycleOwner) {
            when (it) {
                true -> {
                    displaySuccess(requireContext())
                    dismiss()
                }

                false -> displayFailure(requireContext())
                null -> {}
            }
        }

        categoryViewModel.allCategories.observe(viewLifecycleOwner) {
            setAdapter(it)
        }

        categoryViewModel.isCategoryIdentifierPresent.observe(viewLifecycleOwner) {
            when (it) {
                true -> handleCategoryAlreadyPresent()
                false -> createAndInsertCategory()
                null -> {}
            }
        }
    }


    private fun handleCategoryAlreadyPresent() {
        categoryIdentifierTextField.text = null
        val previousHint = categoryIdentifierTextField.hint
        categoryIdentifierTextField.hint = IDENTIFIER_ALREADY_PRESENT_ERROR_MESSAGE
        submitButton.isEnabled = false
        val previousButtonTextColor = submitButton.textColors
        submitButton.setTextColor(Color.RED)
        CoroutineScope(Dispatchers.Main).launch {
            delay(1500)
            categoryIdentifierTextField.hint = previousHint
            categoryIdentifierTextField.setText(categoryIdentifier)
            submitButton.setTextColor(previousButtonTextColor)
            submitButton.isEnabled = true
        }
    }

    private fun createAndInsertCategory() {
        val category = Category(
            UUID.randomUUID(),
            categoryIdentifier,
            System.currentTimeMillis(),
            System.currentTimeMillis()
        )
        categoryViewModel.insertCategory(category)
    }

    private fun setAdapter(categories: List<Category>) {
        val arrayAdapter = context?.let {
            ArrayAdapter(it, android.R.layout.simple_dropdown_item_1line, categories)
        } ?: return
        movementCategoryIdTextField.setAdapter(arrayAdapter)
    }

    private fun changeInputFieldsById(id: Int?) {
        when (id) {
            R.id.input_option_movement -> {
                sectionMovementLinearLayout.visibility = View.VISIBLE
                sectionCategoryLinearLayout.visibility = View.GONE
            }

            R.id.input_option_category -> {
                sectionMovementLinearLayout.visibility = View.GONE
                sectionCategoryLinearLayout.visibility = View.VISIBLE
            }
        }
    }

    private fun handleSubmit() {
        when (selectedOption.value) {
            R.id.input_option_movement -> handleMovementSubmission()
            R.id.input_option_category -> handleCategorySubmission()
        }
    }

    private fun handleMovementSubmission() {
        if (!this::selectedCategory.isInitialized) {
            showTemporaryError(movementCategoryIdTextField)
            return
        }

        val amount = movementAmountTextField.text.toString().toDoubleOrNull()
        if (amount == null || amount == 0.0) {
            showTemporaryError(movementAmountTextField)
            return
        }

        val createdAt = convertToMilliseconds(movementCreatedAtTextField.text.toString())
        if (createdAt < 0) {
            showTemporaryError(movementCreatedAtTextField)
            return
        }

        val movement = Movement(
            UUID.randomUUID(),
            amount,
            selectedCategory.uuid,
            createdAt,
            System.currentTimeMillis()
        )
        movementWithCategoryViewModel.insertMovement(movement)
    }

    private fun handleCategorySubmission() {
        categoryIdentifier = categoryIdentifierTextField.text.toString()
        if (categoryIdentifier.isEmpty()) {
            showTemporaryError(categoryIdentifierTextField)
            return
        }
        // The access to database require an observer (isCategoryIdentifierPresent)
        categoryViewModel.isCategoryIdentifierPresent(categoryIdentifier)
    }


    private fun showTemporaryError(textField: TextView) {
        val previousTextColor = textField.textColors
        val previousButtonTextColor = submitButton.textColors
        textField.setTextColor(Color.RED)
        submitButton.isEnabled = false
        submitButton.setTextColor(Color.RED)
        CoroutineScope(Dispatchers.Main).launch {
            delay(1500)
            textField.setTextColor(previousTextColor)
            submitButton.setTextColor(previousButtonTextColor)
            submitButton.isEnabled = true
        }
    }

    private fun showDateTimePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = context?.let {
            DatePickerDialog(
                it,
                { _, selectedYear, selectedMonth, selectedDayOfMonth ->
                    val selectedDate = String.format(
                        "%02d/%02d/%04d",
                        selectedDayOfMonth,
                        selectedMonth + 1,
                        selectedYear
                    )
                    showTimePickerDialog(selectedDate)
                },
                year,
                month,
                dayOfMonth
            )
        }

        datePickerDialog?.show()
    }

    private fun showTimePickerDialog(selectedDate: String) {
        val calendar = Calendar.getInstance()
        val hourOfDay = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(
            context,
            { _, selectedHourOfDay, selectedMinute ->
                val selectedTime = String.format("%02d:%02d", selectedHourOfDay, selectedMinute)
                val selectedDateTime = "$selectedDate $selectedTime"
                movementCreatedAtTextField.setText(selectedDateTime)
            },
            hourOfDay,
            minute,
            true
        )

        timePickerDialog.show()
    }

    private fun convertToMilliseconds(dateTime: String): Long {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        return try {
            val date = dateFormat.parse(dateTime)
            date?.time ?: 0L
        } catch (e: Exception) {
            -1
        }
    }

}
