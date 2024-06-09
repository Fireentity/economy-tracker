package it.unipd.dei.xml_frontend.ui.dialog

import android.content.Context
import android.content.res.Resources
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.LifecycleOwner
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import it.unipd.dei.common_backend.models.Category
import it.unipd.dei.common_backend.models.MovementBuilder
import it.unipd.dei.common_backend.models.MovementWithCategory
import it.unipd.dei.common_backend.view.CategoryViewModel
import it.unipd.dei.common_backend.view.MovementWithCategoryViewModel
import it.unipd.dei.xml_frontend.R
import it.unipd.dei.xml_frontend.ui.input.MovementCategoryInput
import it.unipd.dei.xml_frontend.ui.buttons.UpsertMovementButton
import it.unipd.dei.xml_frontend.ui.input.MovementAmountInput
import it.unipd.dei.xml_frontend.ui.input.MovementDateInput

class UpsertMovementDialog(
    categoryViewModel: CategoryViewModel,
    movementWithCategoryViewModel: MovementWithCategoryViewModel,
    view: View,
    private val fragmentContext: Context,
    lifecycleOwner: LifecycleOwner,
    title: String,
    movementWithCategory: MovementWithCategory? = null
) : IDialog {

    private val alertDialog: AlertDialog
    private val categoryInputField: MovementCategoryInput
    private val amountInputField: MovementAmountInput
    private val dateInputField: MovementDateInput
    private val movementBuilder: MovementBuilder = MovementBuilder(
        { getMovementAmount() },
        { getMovementCategory() },
        { getMovementDate() },
        movementWithCategory?.movement
    )

    init {
        val inputFieldView: MaterialAutoCompleteTextView =
            view.findViewById(R.id.input_movement_category)
        val updateMovementButton = UpsertMovementButton(
            movementBuilder,
            movementWithCategoryViewModel,
            fragmentContext,
            this::dismiss
        )
        this.dateInputField = MovementDateInput(
            fragmentContext,
            view.findViewById(R.id.input_movement_date),
            movementWithCategory?.movement
        )

        this.amountInputField = MovementAmountInput(
            view.findViewById(R.id.input_movement_amount),
            movementWithCategory?.movement
        )

        this.categoryInputField = MovementCategoryInput(
            categoryViewModel,
            view.findViewById(R.id.input_movement_category_layout),
            inputFieldView,
            lifecycleOwner,
            movementWithCategory
        )



        this.alertDialog = MaterialAlertDialogBuilder(fragmentContext)
            .setTitle(title)
            .setView(view)
            .setPositiveButton(
                R.string.confirm
            ) { _, _ -> updateMovementButton.onClick() }
            .create()

        inputFieldView.setOnItemClickListener { parent, _, position, _ ->
            categoryInputField.onItemClick(
                parent,
                position
            )
        }

        view.setOnClickListener {
            updateMovementButton.onClick()
        }
    }

    private fun getMovementAmount(): Double? = amountInputField.getAmount()
    private fun getMovementCategory(): Category? = categoryInputField.getCategory()
    private fun getMovementDate(): Long? = dateInputField.getDate()
    override fun getFragmentContext() = fragmentContext

    override fun getResources(): Resources = fragmentContext.resources

    override fun show() = alertDialog.show()

    override fun dismiss() = alertDialog.dismiss()
}