package it.unipd.dei.xml_frontend.fragments

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint
import it.unipd.dei.common_backend.models.Summary
import it.unipd.dei.common_backend.utils.DisplayToast
import it.unipd.dei.common_backend.viewModels.CategoryViewModel
import it.unipd.dei.common_backend.viewModels.MovementWithCategoryViewModel
import it.unipd.dei.common_backend.viewModels.SummaryViewModel
import it.unipd.dei.common_backend.viewModels.TestViewModel
import it.unipd.dei.xml_frontend.R
import it.unipd.dei.xml_frontend.ui.adapters.MovementWithSummaryHeaderCardAdapter
import it.unipd.dei.xml_frontend.ui.buttons.ShowAddMovementDialogButton
import it.unipd.dei.xml_frontend.ui.dropdown.menus.CategoryDropdownMenu
import it.unipd.dei.xml_frontend.ui.tabs.AllRegisterTab
import it.unipd.dei.xml_frontend.ui.tabs.ExpensesRegisterTab
import it.unipd.dei.xml_frontend.ui.tabs.RevenueRegisterTab


@AndroidEntryPoint
class RegisterFragment : Fragment() {

    companion object {
        const val DEFAULT_TAB_SELECTED = 1;
    }

    private val testViewModel: TestViewModel by activityViewModels()
    private val movementWithCategoryViewModel: MovementWithCategoryViewModel by activityViewModels()
    private val categoryViewModel: CategoryViewModel by activityViewModels()
    private val summaryViewModel: SummaryViewModel by activityViewModels()
    private var selectedTab : Int = DEFAULT_TAB_SELECTED

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

        val sharedPref = activity?.getPreferences(MODE_PRIVATE)
        sharedPref?.let {
            selectedTab = sharedPref.getInt(getString(R.string.saved_selected_tab), DEFAULT_TAB_SELECTED)
        }

        // Inflate the layout
        val view = inflater.inflate(R.layout.fragment_register, container, false)
        val floatingActionButton: View = view.findViewById(R.id.show_add_movement_dialog_button)


        val dialogView = inflater.inflate(
            R.layout.fragment_movement_dialog,
            container,
            false
        )
        val showAddMovementDialogButton = ShowAddMovementDialogButton(
            dialogView,
            categoryViewModel,
            movementWithCategoryViewModel,
            summaryViewModel,
            requireContext(),
            viewLifecycleOwner,
            parentFragmentManager
        )
        floatingActionButton.setOnClickListener {
            showAddMovementDialogButton.onClick()
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        CategoryDropdownMenu(
            categoryViewModel,
            movementWithCategoryViewModel,
            view.findViewById(R.id.categories_dropdown_selection),
            viewLifecycleOwner,
            requireContext()
        )

        val tabs = listOf(
            RevenueRegisterTab(
                summaryViewModel,
                movementWithCategoryViewModel,
                view.findViewById(R.id.positive_movements_recyclerview),
                MovementWithSummaryHeaderCardAdapter(
                    summaryViewModel.currentMonthSummary.value ?: Summary.DEFAULT,
                    movementWithCategoryViewModel.getPositiveMovements().value ?: emptyList(),
                    parentFragmentManager,
                    requireContext()
                ),
                viewLifecycleOwner
            ),
            AllRegisterTab(
                summaryViewModel,
                movementWithCategoryViewModel,
                view.findViewById(R.id.all_movements_recyclerview),
                MovementWithSummaryHeaderCardAdapter(
                    summaryViewModel.currentMonthSummary.value ?: Summary.DEFAULT,
                    movementWithCategoryViewModel.getMovements().value ?: emptyList(),
                    parentFragmentManager,
                    requireContext()
                ),
                viewLifecycleOwner
            ),
            ExpensesRegisterTab(
                summaryViewModel,
                movementWithCategoryViewModel,
                view.findViewById(R.id.negative_movements_recyclerview),
                MovementWithSummaryHeaderCardAdapter(
                    summaryViewModel.currentMonthSummary.value ?: Summary.DEFAULT,
                    movementWithCategoryViewModel.getPositiveMovements().value ?: emptyList(),
                    parentFragmentManager,
                    requireContext()
                ),
                viewLifecycleOwner
            )
        )

        val tabLayout: TabLayout = view.findViewById(R.id.register_tab_layout)

        tabLayout.getTabAt(selectedTab)?.select()
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                selectedTab = tab?.position ?: DEFAULT_TAB_SELECTED
                tabs[tab?.position ?: DEFAULT_TAB_SELECTED].show()
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                tabs[tab?.position ?: DEFAULT_TAB_SELECTED].hide()
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                tabs[tab?.position ?: DEFAULT_TAB_SELECTED].scrollToStart()
            }
        })

        testViewModel.createDummyDataIfNoMovement()
        categoryViewModel.loadAllCategories()
        summaryViewModel.loadSummaryForCurrentMonth()
    }

    override fun onPause() {
        super.onPause()
        val sharedPref = activity?.getPreferences(MODE_PRIVATE) ?: return
        with(sharedPref.edit()) {
            putInt(getString(R.string.saved_selected_tab), selectedTab)
            apply()
        }
    }
}
