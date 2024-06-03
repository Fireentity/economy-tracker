package it.unipd.dei.music_application.ui.dialog.helper

import android.content.Context
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import it.unipd.dei.music_application.models.Category
import it.unipd.dei.music_application.models.Movement
import java.util.UUID

object MovementInputHelper {
        fun setupMovementAmountTextField(movementAmountTextField: EditText) {
            val decimalDigitsInputFilter = InputFilter { source, _, _, dest, _, _ ->
                val inputText = dest.toString() + source.toString()
                if (inputText.matches(Regex("^(-)?\\d{0,6}(\\.\\d{0,2})?$"))) null else ""
            }
            movementAmountTextField.filters = arrayOf(decimalDigitsInputFilter)

            movementAmountTextField.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

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

        fun validateAndReturnMovement(
            category: Category?,
            movementAmountTextField: EditText,
            movementSubmitButton: Button,
            movementCategoryIdTextField: AutoCompleteTextView,
            movementCreatedAtTextField: EditText
        ): Movement? {
            if (category == null) {
                InputHelper.showTemporaryError(movementCategoryIdTextField, movementSubmitButton)
                return null
            }

            val amount = movementAmountTextField.text.toString().toDoubleOrNull()
            if (amount == null || amount == 0.0) {
                InputHelper.showTemporaryError(movementAmountTextField, movementSubmitButton)
                return null
            }

            val createdAt =
                InputHelper.convertToMilliseconds(movementCreatedAtTextField.text.toString())
            if (createdAt < 0) {
                InputHelper.showTemporaryError(movementCreatedAtTextField, movementSubmitButton)
                return null
            }

            return Movement(
                UUID.randomUUID(),
                amount,
                category.uuid,
                createdAt,
                System.currentTimeMillis()
            )
        }

        fun setAdapter(
            context: Context,
            categories: List<Category>,
            movementCategoryIdTextField: AutoCompleteTextView
        ) {
            val arrayAdapter = ArrayAdapter(context, android.R.layout.simple_dropdown_item_1line, categories)
            movementCategoryIdTextField.setAdapter(arrayAdapter)
        }
}