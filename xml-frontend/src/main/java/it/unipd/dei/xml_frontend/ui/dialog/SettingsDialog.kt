package it.unipd.dei.xml_frontend.ui.dialog

import android.app.Activity
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.DialogInterface
import android.content.res.Resources
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.google.android.material.button.MaterialButton
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.materialswitch.MaterialSwitch
import it.unipd.dei.common_backend.enums.Currency
import it.unipd.dei.common_backend.utils.DisplayToast
import it.unipd.dei.xml_frontend.R

class SettingsDialog(
    private val activity: Activity,
    private val fragmentContext: Context,
    dialogView: View
) : IDialog {
    private val alertDialog: AlertDialog
    private val switchView: MaterialSwitch = dialogView.findViewById(R.id.theme_switch)
    private val dollarButton: MaterialButton = dialogView.findViewById(R.id.button_dollar)
    private val euroButton: MaterialButton = dialogView.findViewById(R.id.button_euro)
    private val poundButton: MaterialButton = dialogView.findViewById(R.id.button_pound)

    private val sharedPref = activity.getPreferences(MODE_PRIVATE)
    private val defaultCurrency: Currency= Currency.fromInt(
        sharedPref.getInt(
            fragmentContext.getString(R.string.saved_selected_currency),
            Currency.EURO.value
        )
    ) ?: Currency.EURO
    private var selectedCurrency: Currency = defaultCurrency

    init {

        val isDarkModeEnable: Boolean = sharedPref.getBoolean(
            fragmentContext.getString(R.string.is_dark_mode_enable),
            false
        )

        when (defaultCurrency) {
            Currency.EURO -> euroButton.isChecked = true
            Currency.DOLLAR -> dollarButton.isChecked = true
            Currency.POUND -> poundButton.isChecked = true
        }

        if (isDarkModeEnable) {
            switchView.isChecked = true
        }

        euroButton.setOnClickListener {
            selectedCurrency = Currency.EURO
        }
        dollarButton.setOnClickListener {
            selectedCurrency = Currency.DOLLAR
        }
        poundButton.setOnClickListener {
            selectedCurrency = Currency.POUND
        }

        alertDialog = MaterialAlertDialogBuilder(fragmentContext)
            .setTitle(fragmentContext.getString(R.string.settings))
            .setView(dialogView)
            .setPositiveButton(fragmentContext.getString(R.string.confirm)) { _: DialogInterface, _: Int ->
                if (selectedCurrency != defaultCurrency) {
                    saveCurrency()
                }
                if (switchView.isChecked != isDarkModeEnable) {
                    saveDarkMode()
                }
            }
            .setNegativeButton(fragmentContext.getString(R.string.cancel)) { _: DialogInterface, _: Int ->
                dismiss()
            }
            .create()
    }

    private fun saveDarkMode() {
        with(sharedPref.edit()) {
            putBoolean(
                fragmentContext.getString(R.string.is_dark_mode_enable),
                switchView.isChecked
            )
            apply()
        }
        activity.recreate()
    }


    private fun saveCurrency() {
        with(sharedPref.edit()) {
            putInt(
                fragmentContext.getString(R.string.saved_selected_currency),
                selectedCurrency.value
            )
            apply()
        }
        activity.recreate()
    }

    override fun getFragmentContext(): Context = fragmentContext

    override fun getResources(): Resources = fragmentContext.resources

    override fun show() = alertDialog.show()

    override fun dismiss() = alertDialog.dismiss()
}