package it.unipd.dei.xml_frontend.ui.dialog

import android.app.Activity
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.res.Resources
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.google.android.material.button.MaterialButton
import com.google.android.material.button.MaterialButtonToggleGroup
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.materialswitch.MaterialSwitch
import it.unipd.dei.common_backend.enums.Currency
import it.unipd.dei.common_backend.utils.DisplayToast
import it.unipd.dei.xml_frontend.R
import it.unipd.dei.xml_frontend.fragments.RegisterFragment.Companion.DEFAULT_TAB_SELECTED

class SettingsDialog(
    private val activity: Activity,
    private val fragmentContext: Context,
    dialogView: View
) : IDialog {
    private val alertDialog: AlertDialog
    private val textView: TextView = dialogView.findViewById(R.id.theme_text)
    private val switchView: MaterialSwitch = dialogView.findViewById(R.id.theme_switch)
    private val dollarButton: MaterialButton = dialogView.findViewById(R.id.button_dollar)
    private val euroButton: MaterialButton = dialogView.findViewById(R.id.button_euro)
    private val poundButton: MaterialButton = dialogView.findViewById(R.id.button_pound)
    private val toggleGroup: MaterialButtonToggleGroup = dialogView.findViewById(R.id.toggle_group)

    private var selectedCurrency: Currency? = null

    init {
        alertDialog = MaterialAlertDialogBuilder(fragmentContext)
            .setTitle(fragmentContext.getString(R.string.settings))
            .setView(dialogView)
            .setOnDismissListener {
                saveSharedPreferences()
            }
            .create()
        val sharedPref = activity.getPreferences(MODE_PRIVATE)
        var isDarkModeEnable = false
        sharedPref?.let {
            try {
                selectedCurrency = Currency.fromInt(
                    sharedPref.getInt(
                        fragmentContext.getString(R.string.saved_selected_currency),
                        Currency.EURO.value
                    )
                )
                isDarkModeEnable =
                    sharedPref.getBoolean(fragmentContext.getString(R.string.is_dark_mode_enable), false)
            } catch (_: ClassCastException) {
            }
        }

        when (selectedCurrency) {
            Currency.EURO -> euroButton.isChecked = true
            Currency.DOLLAR -> dollarButton.isChecked = true
            Currency.POUND -> poundButton.isChecked = true
            null -> {}
        }

        if(isDarkModeEnable) {
            switchView.isChecked = true
        }


        textView.text = fragmentContext.getString(R.string.dark_mode)
        euroButton.setOnClickListener {
            selectedCurrency = Currency.EURO
        }
        dollarButton.setOnClickListener {
            selectedCurrency = Currency.DOLLAR
        }
        poundButton.setOnClickListener {
            selectedCurrency = Currency.POUND
        }


    }

    private fun saveSharedPreferences() {

        val sharedPref = activity.getPreferences(MODE_PRIVATE)
        if (sharedPref == null) {
            DisplayToast.displayGeneric(
                fragmentContext,
                fragmentContext.getString(R.string.settings_update_failed)
            )
            return
        }
        if (selectedCurrency == null) {
            DisplayToast.displayGeneric(
                fragmentContext,
                fragmentContext.getString(R.string.settings_update_failed)
            )
            return
        }

        with(sharedPref.edit()) {
            putInt(
                fragmentContext.getString(R.string.saved_selected_currency),
                selectedCurrency!!.value
            )
            putBoolean(
                fragmentContext.getString(R.string.is_dark_mode_enable),
                switchView.isChecked
            )
            apply()
        }
        DisplayToast.displayGeneric(
            fragmentContext,
            fragmentContext.getString(R.string.settings_update_successful)
        )
    }

    override fun getFragmentContext(): Context = fragmentContext

    override fun getResources(): Resources = fragmentContext.resources

    override fun show() = alertDialog.show()

    override fun dismiss() = alertDialog.dismiss()
}