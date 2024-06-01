package it.unipd.dei.music_application.ui.register

import android.view.View
import com.google.android.material.tabs.TabLayout
import it.unipd.dei.music_application.enums.CategoryTab

class Register(
    private val allTab: List<RegisterTab>,
    tabLayout: TabLayout
) {
    companion object {
        private const val DEFAULT_SELECTED_TAB = 10
    }

    init {
        tabLayout.getTabAt(DEFAULT_SELECTED_TAB)?.select()
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                allTab[tab?.position?:0].show()
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                allTab[tab?.position?:0].hide()
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                allTab[tab?.position?:0].show()
            }
        })
    }
}