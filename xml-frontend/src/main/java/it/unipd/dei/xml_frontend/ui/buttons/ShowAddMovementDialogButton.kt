package it.unipd.dei.xml_frontend.ui.buttons

import android.content.Context
import android.view.View
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import it.unipd.dei.common_backend.viewModels.CategoryViewModel
import it.unipd.dei.common_backend.viewModels.MovementWithCategoryViewModel
import it.unipd.dei.common_backend.viewModels.SummaryViewModel
import it.unipd.dei.xml_frontend.R
import it.unipd.dei.xml_frontend.ui.dialog.UpsertMovementDialog

class ShowAddMovementDialogButton(
    dialogView: View,
    categoryViewModel: CategoryViewModel,
    movementWithCategoryViewModel: MovementWithCategoryViewModel,
    summaryViewModel: SummaryViewModel,
    fragmentContext: Context,
    lifecycleOwner: LifecycleOwner,
    fragmentManager: FragmentManager
) : IButton {

    private val addMovementDialog = UpsertMovementDialog(
        categoryViewModel,
        movementWithCategoryViewModel,
        summaryViewModel,
        dialogView,
        fragmentContext,
        lifecycleOwner,
        fragmentContext.getString(R.string.create_movement),
        fragmentManager
    )

    override fun onClick() {
        addMovementDialog.show()
    }
}