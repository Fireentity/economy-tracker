package it.unipd.dei.xml_frontend.ui.dialog

import android.content.Context
import android.content.res.Resources
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import it.unipd.dei.common_backend.models.MovementBuilder
import it.unipd.dei.common_backend.view.CategoryViewModel
import it.unipd.dei.common_backend.view.MovementWithCategoryViewModel
import it.unipd.dei.xml_frontend.R
import it.unipd.dei.xml_frontend.ui.buttons.AddMovementButton
import it.unipd.dei.xml_frontend.ui.input.MovementCategoryInput

class AddMovementDialog(
    categoryViewModel: CategoryViewModel,
    movementWithCategoryViewModel: MovementWithCategoryViewModel,
    view: View,
    private val fragmentContext: Context,
) : IDialog {

    private val alertDialog: AlertDialog
    private val inputField: MovementCategoryInput
    private val movementBuilder: MovementBuilder = MovementBuilder()

    init {
        //TODO add the movement amount field
        val movementCategoryFieldView: MaterialAutoCompleteTextView =
            view.findViewById(R.id.input_movement_category)
        val addMovementButton = AddMovementButton(
            movementBuilder,
            this::dismiss,
            fragmentContext,
            movementWithCategoryViewModel
        )

        this.inputField = MovementCategoryInput(
            categoryViewModel,
            movementCategoryFieldView
        )
        this.alertDialog = MaterialAlertDialogBuilder(fragmentContext)
            .setView(view)
            .setPositiveButton(
                R.string.confirm
            ) { _, _ -> addMovementButton.onClick() }
            .create()

        view.setOnClickListener {
            addMovementButton.onClick()
        }
    }

    override fun getFragmentContext() = fragmentContext

    override fun getResources(): Resources = fragmentContext.resources

    override fun show() = alertDialog.show()

    override fun dismiss() = alertDialog.dismiss()
}