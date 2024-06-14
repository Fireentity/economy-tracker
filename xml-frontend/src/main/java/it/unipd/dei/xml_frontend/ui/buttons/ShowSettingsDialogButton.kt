package it.unipd.dei.xml_frontend.ui.buttons

import android.app.Activity
import android.content.Context
import android.view.View
import it.unipd.dei.xml_frontend.ui.dialog.SettingsDialog

class ShowSettingsDialogButton(
    activity: Activity,
    fragmentContext: Context,
    dialogView: View
) : IButton {
    private val settingsDialog = SettingsDialog(
        activity,
        fragmentContext,
        dialogView
    )

    override fun onClick() {
        settingsDialog.show()
    }
}