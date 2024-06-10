package it.unipd.dei.xml_frontend.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint
import it.unipd.dei.common_backend.viewModels.CategoryViewModel
import it.unipd.dei.common_backend.viewModels.MovementWithCategoryViewModel
import it.unipd.dei.common_backend.viewModels.TestViewModel
import it.unipd.dei.xml_frontend.R
import it.unipd.dei.xml_frontend.ui.MovementCardAdapter
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

    private val testViewModel: TestViewModel by viewModels()
    private val movementWithCategoryViewModel: MovementWithCategoryViewModel by viewModels()
    private val categoryViewModel: CategoryViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

        // Create dummy data if no movement exists
        testViewModel.createDummyDataIfNoMovement()
        categoryViewModel.loadAllCategories()

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
            requireContext(),
            viewLifecycleOwner
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
                movementWithCategoryViewModel,
                view.findViewById(R.id.positive_movements_recycler_view),
                MovementCardAdapter(
                    movementWithCategoryViewModel.getPositiveMovement().value ?: emptyList(),
                    parentFragmentManager,
                    movementWithCategoryViewModel,
                    categoryViewModel
                ),
                viewLifecycleOwner
            ),
            AllRegisterTab(
                movementWithCategoryViewModel,
                view.findViewById(R.id.all_movements_recycler_view),
                MovementCardAdapter(
                    movementWithCategoryViewModel.getMovements().value ?: emptyList(),
                    parentFragmentManager,
                    movementWithCategoryViewModel,
                    categoryViewModel
                ),
                viewLifecycleOwner
            ),
            ExpensesRegisterTab(
                movementWithCategoryViewModel,
                view.findViewById(R.id.negative_movements_recycler_view),
                MovementCardAdapter(
                    movementWithCategoryViewModel.getNegativeMovement().value ?: emptyList(),
                    parentFragmentManager,
                    movementWithCategoryViewModel,
                    categoryViewModel
                ),
                viewLifecycleOwner
            )
        )

        val tabLayout: TabLayout = view.findViewById(R.id.register_tab_layout)

        tabLayout.getTabAt(DEFAULT_TAB_SELECTED)?.select()
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                tabs[tab?.position ?: 0].show()
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                tabs[tab?.position ?: 0].hide()
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                tabs[tab?.position ?: 0].show()
            }
        })
    }
}
