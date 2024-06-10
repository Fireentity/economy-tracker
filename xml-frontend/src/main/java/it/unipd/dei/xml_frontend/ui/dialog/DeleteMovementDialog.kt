package it.unipd.dei.xml_frontend.ui.dialog

import android.content.Context
import android.content.res.Resources
import androidx.appcompat.app.AlertDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import it.unipd.dei.common_backend.models.MovementWithCategory
import it.unipd.dei.common_backend.utils.DisplayToast
import it.unipd.dei.common_backend.viewModels.MovementWithCategoryViewModel
import it.unipd.dei.xml_frontend.R

class DeleteMovementDialog(
    movementWithCategoryViewModel: MovementWithCategoryViewModel,
    private val fragmentContext: Context,
    movement: MovementWithCategory
) : IDialog {

    private val alertDialog: AlertDialog

    init {
        alertDialog = MaterialAlertDialogBuilder(fragmentContext)
            .setTitle(fragmentContext.resources.getString(R.string.delete_movement))
            .setMessage(fragmentContext.resources.getString(R.string.movement_deletion_confirmation))
            .setNeutralButton(fragmentContext.resources.getString(R.string.cancel)) { _, _ -> }
            .setPositiveButton(fragmentContext.resources.getString(R.string.confirm)) { _, _ ->
                movementWithCategoryViewModel.deleteMovement(
                    movement,
                    {
                        DisplayToast.displaySuccess(fragmentContext)
                    },
                    { DisplayToast.displayFailure(fragmentContext) }
                )
            }
            .create()
    }

    override fun getFragmentContext(): Context = fragmentContext

    override fun getResources(): Resources = fragmentContext.resources

    override fun show() = alertDialog.show()

    override fun dismiss() = alertDialog.dismiss()
}