package it.unipd.dei.xml_frontend.ui.dialog

import android.content.Context
import android.content.res.Resources
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import it.unipd.dei.common_backend.models.MovementBuilder
import it.unipd.dei.common_backend.models.MovementWithCategory
import it.unipd.dei.common_backend.viewModels.CategoryViewModel
import it.unipd.dei.common_backend.viewModels.MovementWithCategoryViewModel
import it.unipd.dei.common_backend.viewModels.SummaryViewModel
import it.unipd.dei.xml_frontend.R
import it.unipd.dei.xml_frontend.ui.buttons.UpsertMovementButton
import it.unipd.dei.xml_frontend.ui.input.MovementAmountInput
import it.unipd.dei.xml_frontend.ui.input.MovementCategoryInput
import it.unipd.dei.xml_frontend.ui.input.MovementDateInput

class UpsertMovementDialog(
    categoryViewModel: CategoryViewModel,
    movementWithCategoryViewModel: MovementWithCategoryViewModel,
    summaryViewModel: SummaryViewModel,
    view: View,
    private val fragmentContext: Context,
    lifecycleOwner: LifecycleOwner,
    title: String,
    fragmentManager: FragmentManager,
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
        val upsertMovementButton = UpsertMovementButton(
            movementBuilder,
            categoryViewModel,
            movementWithCategoryViewModel,
            summaryViewModel,
            fragmentContext,
            this::dismiss
        )
        this.dateInputField = MovementDateInput(
            view.findViewById(R.id.input_movement_date),
            fragmentManager,
            fragmentContext,
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
            ) { _, _ -> upsertMovementButton.onClick() }
            .create()

        inputFieldView.setOnItemClickListener { parent, _, position, _ ->
            categoryInputField.onItemClick(
                parent,
                position
            )
        }

        view.setOnClickListener {
            upsertMovementButton.onClick()
        }
    }

    private fun getMovementAmount(): String = amountInputField.getAmount()
    private fun getMovementCategory(): String = categoryInputField.getCategory()
    private fun getMovementDate(): Long? = dateInputField.getDate()
    override fun getFragmentContext() = fragmentContext

    override fun getResources(): Resources = fragmentContext.resources

    override fun show() = alertDialog.show()

    override fun dismiss() = alertDialog.dismiss()
}