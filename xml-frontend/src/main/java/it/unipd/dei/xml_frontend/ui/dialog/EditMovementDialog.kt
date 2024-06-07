package it.unipd.dei.xml_frontend.ui.dialog

import android.content.Context
import android.content.res.Resources
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import it.unipd.dei.common_backend.models.MovementWithCategory
import it.unipd.dei.common_backend.view.CategoryViewModel
import it.unipd.dei.common_backend.view.MovementWithCategoryViewModel
import it.unipd.dei.xml_frontend.R
import it.unipd.dei.xml_frontend.ui.input.MovementCategoryInput
import it.unipd.dei.xml_frontend.ui.buttons.UpdateMovementButton

class EditMovementDialog(
    movement: MovementWithCategory,
    movementWithCategoryViewModel: MovementWithCategoryViewModel,
    categoryViewModel: CategoryViewModel,
    view: View,
    private val fragmentContext: Context,
) : IDialog {

    private val alertDialog: AlertDialog
    private val inputField: MovementCategoryInput

    init {
        val inputFieldView: MaterialAutoCompleteTextView =
            view.findViewById(R.id.input_movement_category)
        val updateMovementButton = UpdateMovementButton(
            movement,
            movementWithCategoryViewModel,
            fragmentContext,
            this::dismiss
        )

        this.inputField = MovementCategoryInput(
            movement,
            categoryViewModel,
            inputFieldView
        )
        this.alertDialog = MaterialAlertDialogBuilder(fragmentContext)
            .setView(view)
            .setPositiveButton(
                R.string.confirm
            ) { _, _ -> updateMovementButton.onClick() }
            .create()

        inputFieldView.setOnItemClickListener { parent, _, position, _ ->
            inputField.onItemClick(
                parent,
                position
            )
        }

        view.setOnClickListener {
            updateMovementButton.onClick()
        }
    }

    override fun getFragmentContext() = fragmentContext

    override fun getResources(): Resources = fragmentContext.resources

    override fun show() = alertDialog.show()

    override fun dismiss() = alertDialog.dismiss()
}