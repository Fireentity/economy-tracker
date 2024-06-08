package it.unipd.dei.xml_frontend.ui.dialog

import android.content.Context
import android.content.res.Resources
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.LifecycleOwner
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import com.google.android.material.textfield.TextInputLayout
import it.unipd.dei.common_backend.models.Category
import it.unipd.dei.common_backend.models.MovementBuilder
import it.unipd.dei.common_backend.view.CategoryViewModel
import it.unipd.dei.common_backend.view.MovementWithCategoryViewModel
import it.unipd.dei.xml_frontend.R
import it.unipd.dei.xml_frontend.ui.buttons.AddMovementButton
import it.unipd.dei.xml_frontend.ui.input.MovementAmountInput
import it.unipd.dei.xml_frontend.ui.input.MovementCategoryInput
import it.unipd.dei.xml_frontend.ui.input.MovementDateInput
import java.sql.DatabaseMetaData

class AddMovementDialog(
    categoryViewModel: CategoryViewModel,
    movementWithCategoryViewModel: MovementWithCategoryViewModel,
    view: View,
    private val fragmentContext: Context,
    lifecycleOwner: LifecycleOwner
) : IDialog {

    private val alertDialog: AlertDialog
    private val categoryInputField: MovementCategoryInput
    private val amountInputField: MovementAmountInput
    private val dateInputField: MovementDateInput
    private val movementBuilder: MovementBuilder = MovementBuilder(
        { getMovementAmount() },
        { getMovementCategory() },
        { getMovementDate() }
    )

    init {

        val movementCategoryFieldView: MaterialAutoCompleteTextView =
            view.findViewById(R.id.input_movement_category)

        val addMovementButton = AddMovementButton(
            movementBuilder,
            this::dismiss,
            fragmentContext,
            movementWithCategoryViewModel
        )

        this.dateInputField = MovementDateInput(
            fragmentContext,
            view.findViewById(R.id.input_movement_date)
        )

        this.amountInputField = MovementAmountInput(
            view.findViewById(R.id.input_movement_amount)
        )

        this.categoryInputField = MovementCategoryInput(
            categoryViewModel,
            view.findViewById(R.id.input_movement_category_layout),
            movementCategoryFieldView,
            lifecycleOwner
        )
        this.alertDialog = MaterialAlertDialogBuilder(fragmentContext)
            .setView(view)
            .setPositiveButton(
                R.string.confirm
            ) { _, _ -> addMovementButton.onClick() }
            .create()

        //TODO chiedere cosa fa questo
        view.setOnClickListener {
            addMovementButton.onClick()
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