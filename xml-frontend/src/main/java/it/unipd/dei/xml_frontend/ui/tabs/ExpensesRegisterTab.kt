package it.unipd.dei.xml_frontend.ui.tabs

import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import it.unipd.dei.common_backend.viewModels.MovementWithCategoryViewModel
import it.unipd.dei.xml_frontend.ui.adapters.MovementCardAdapter

class ExpensesRegisterTab(
    viewModel: MovementWithCategoryViewModel,
    recyclerView: RecyclerView, movementCardAdapter: MovementCardAdapter,
    lifecycleOwner: LifecycleOwner,
    ) : RegisterTab(
    viewModel, recyclerView, movementCardAdapter, lifecycleOwner
) {
    override fun observeViewModel(lifecycleOwner: LifecycleOwner) {
        viewModel.getNegativeMovements().observe(lifecycleOwner){
           movementCardAdapter.updateMovements(it)
        }
    }


    override fun loadSomeMovementsByCategory(function: () -> Unit) {
        viewModel.loadSomeNegativeMovementsByCategory(function)
    }
}