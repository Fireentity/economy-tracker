package it.unipd.dei.xml_frontend.ui.dialog.category

import android.content.Context
import android.content.res.Resources

interface IDialog {
    fun getFragmentContext(): Context
    fun getResources(): Resources
    fun show()
    fun dismiss()
}