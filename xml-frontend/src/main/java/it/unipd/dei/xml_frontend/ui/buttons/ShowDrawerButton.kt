package it.unipd.dei.xml_frontend.ui.buttons

import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import it.unipd.dei.xml_frontend.MainActivity

class ShowDrawerButton(private val activity: MainActivity):IButton {

    override fun onClick() {
        val drawerLayout: DrawerLayout = activity.getDrawerLayout()
        if (drawerLayout.isDrawerOpen(GravityCompat.START))
            drawerLayout.closeDrawer(GravityCompat.START)
        else
            drawerLayout.openDrawer(GravityCompat.START)

    }
}