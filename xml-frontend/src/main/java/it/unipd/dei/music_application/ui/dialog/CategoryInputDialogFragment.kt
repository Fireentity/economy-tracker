package it.unipd.dei.music_application.ui.dialog

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import dagger.hilt.android.AndroidEntryPoint
import it.unipd.dei.music_application.R
import it.unipd.dei.music_application.models.Category
import it.unipd.dei.music_application.models.Movement
import it.unipd.dei.music_application.models.MovementWithCategory
import it.unipd.dei.music_application.ui.dialog.helper.InputHelper
import it.unipd.dei.music_application.ui.dialog.helper.MovementInputHelper
import it.unipd.dei.music_application.utils.Constants
import it.unipd.dei.music_application.utils.DisplayToast
import it.unipd.dei.music_application.view.CategoryViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.UUID

@AndroidEntryPoint
class CategoryInputDialogFragment(
    private val titleText: String? = null,
    private val buttonText: String? = null,
    private val category: Category? = null
) :
    DialogFragment() {

    private val categoryViewModel: CategoryViewModel by viewModels()

    private lateinit var identifierTextField: EditText
    private lateinit var submitButton: Button
    private lateinit var titleTextField: TextView

    private lateinit var categoryIdentifier: String
    override fun getTheme() = R.style.RoundedCornersDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_input_category, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeInputFields(view)
        categoryViewModel.getAllCategories()
        observeLiveData()
        setParametersToInputFields()
    }

    private fun initializeInputFields(view: View) {
        identifierTextField = view.findViewById(R.id.input_category_identifier)
        submitButton = view.findViewById(R.id.input_category_button)
        titleTextField = view.findViewById(R.id.input_category_title)

        if (titleText != null) {
            titleTextField.text = titleText
        }

        if (buttonText != null) {
            submitButton.text = buttonText
        }
        submitButton.setOnClickListener {
            handleCategorySubmission()
        }
    }

    private fun observeLiveData() {
        categoryViewModel.isCategoryIdentifierPresent.observe(viewLifecycleOwner) {
            when (it) {
                true -> handleCategoryAlreadyPresent()
                false -> upsertCategory()
                null -> {}
            }
        }
        categoryViewModel.upsertResult.observe(viewLifecycleOwner) {
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
    }

    private fun handleCategorySubmission() {
        categoryIdentifier = identifierTextField.text.toString()
        if (categoryIdentifier.isEmpty()) {
            InputHelper.showTemporaryError(identifierTextField, submitButton)
            return
        }
        categoryViewModel.isCategoryIdentifierPresent(categoryIdentifier)
    }

    private fun handleCategoryAlreadyPresent() {
        identifierTextField.text = null
        val previousHint = identifierTextField.hint
        identifierTextField.hint = Constants.IDENTIFIER_ALREADY_PRESENT_ERROR_MESSAGE
        submitButton.isEnabled = false
        val previousButtonTextColor = submitButton.textColors
        submitButton.setTextColor(Color.RED)
        CoroutineScope(Dispatchers.Main).launch {
            delay(1500)
            identifierTextField.hint = previousHint
            identifierTextField.setText(categoryIdentifier)
            submitButton.setTextColor(previousButtonTextColor)
            submitButton.isEnabled = true
        }
    }

    private fun upsertCategory() {
        lateinit var upsertCategory: Category
        if (category == null) {
            upsertCategory = Category(
                UUID.randomUUID(),
                categoryIdentifier,
                System.currentTimeMillis(),
                System.currentTimeMillis()
            )

        } else {
            upsertCategory = Category(
                category.uuid,
                categoryIdentifier,
                category.createdAt,
                System.currentTimeMillis()
            )
        }
        categoryViewModel.upsertCategory(upsertCategory)
    }

    private fun setParametersToInputFields() {
        if (category == null) {
            return
        }
        identifierTextField.setText(category.identifier)
    }

}