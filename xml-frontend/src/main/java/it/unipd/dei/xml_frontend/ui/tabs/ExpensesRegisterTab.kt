package it.unipd.dei.xml_frontend.ui.tabs

import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import it.unipd.dei.common_backend.viewModels.MovementWithCategoryViewModel
import it.unipd.dei.common_backend.viewModels.SummaryViewModel
import it.unipd.dei.xml_frontend.ui.adapters.MovementWithSummaryHeaderCardAdapter

class ExpensesRegisterTab(
    summaryViewModel: SummaryViewModel,
    movementWithCategoryViewModel: MovementWithCategoryViewModel,
    recyclerView: RecyclerView,
    movementWithSummaryHeaderCardAdapter: MovementWithSummaryHeaderCardAdapter,
    lifecycleOwner: LifecycleOwner,
) : RegisterTab(
    summaryViewModel,
    movementWithCategoryViewModel,
    recyclerView,
    movementWithSummaryHeaderCardAdapter,
    lifecycleOwner
) {
    override fun observeViewModel(lifecycleOwner: LifecycleOwner) {
        super.observeViewModel(lifecycleOwner)
        movementWithCategoryViewModel.getNegativeMovements().observe(lifecycleOwner) {
            movementWithSummaryHeaderCardAdapter.updateMovements(it)
        }
    }


    override fun loadSomeMovementsByCategory(function: () -> Unit) {
        movementWithCategoryViewModel.loadSomeNegativeMovementsByCategory(function)
    }
}