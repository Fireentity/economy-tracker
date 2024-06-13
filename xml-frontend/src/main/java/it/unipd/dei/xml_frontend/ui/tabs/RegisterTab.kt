package it.unipd.dei.xml_frontend.ui.tabs

import android.view.View
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import it.unipd.dei.common_backend.viewModels.MovementWithCategoryViewModel
import it.unipd.dei.common_backend.viewModels.SummaryViewModel
import it.unipd.dei.xml_frontend.ui.adapters.MovementWithSummaryHeaderCardAdapter

abstract class RegisterTab(
    private val summaryViewModel: SummaryViewModel,
    protected val movementWithCategoryViewModel: MovementWithCategoryViewModel,
    private val recyclerView: RecyclerView,
    protected val movementWithSummaryHeaderCardAdapter: MovementWithSummaryHeaderCardAdapter,
    lifecycleOwner: LifecycleOwner
) {
    private var loading: Boolean = false

    companion object {
        private const val VISIBLE_THRESHOLD = 3;
    }

    init {
        recyclerView.adapter = movementWithSummaryHeaderCardAdapter
        this.observeViewModel(lifecycleOwner)
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0 && !loading && recyclerView.visibility == View.VISIBLE) {
                    super.onScrolled(recyclerView, dx, dy)
                    val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                    val totalItemCount = layoutManager.itemCount
                    val lastVisibleItem = layoutManager.findLastVisibleItemPosition()
                    if (totalItemCount <= (lastVisibleItem + VISIBLE_THRESHOLD)) {
                        loading = true
                        loadSomeMovementsByCategory {
                            loading = false
                        }
                    }
                }
            }
        })

        loading = true
        this.loadSomeMovementsByCategory {
            loading = false
        }
    }


    open fun observeViewModel(lifecycleOwner: LifecycleOwner){
        summaryViewModel.currentMonthSummary.observe(lifecycleOwner){
            movementWithSummaryHeaderCardAdapter.updateSummary(it)
        }
    }

    abstract fun loadSomeMovementsByCategory(function: () -> Unit)

    fun show() {
        if (movementWithSummaryHeaderCardAdapter.itemCount > 0) {
            recyclerView.scrollToPosition(0)
        }
        recyclerView.visibility = View.VISIBLE;
    }

    fun hide() {
        recyclerView.visibility = View.GONE;
    }
}