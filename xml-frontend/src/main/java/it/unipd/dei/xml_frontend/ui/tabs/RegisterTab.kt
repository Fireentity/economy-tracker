package it.unipd.dei.xml_frontend.ui.tabs

import android.view.View
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import it.unipd.dei.common_backend.view.MovementWithCategoryViewModel
import it.unipd.dei.xml_frontend.ui.MovementCardAdapter

abstract class RegisterTab(
    protected val viewModel: MovementWithCategoryViewModel,
    private val recyclerView: RecyclerView,
    protected val movementCardAdapter: MovementCardAdapter,
    lifecycleOwner: LifecycleOwner
) {
    private var loading: Boolean = false

    companion object {
        private const val VISIBLE_THRESHOLD = 3;
    }

    init {
        recyclerView.apply {
            adapter = movementCardAdapter
            layoutManager = LinearLayoutManager(context)
        }
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

    abstract fun observeViewModel(lifecycleOwner: LifecycleOwner)

    abstract fun loadSomeMovementsByCategory(function: () -> Unit)

    fun show() {
        if (movementCardAdapter.itemCount > 0) {
            recyclerView.scrollToPosition(0)
        }
        recyclerView.visibility = View.VISIBLE;
    }

    fun hide() {
        recyclerView.visibility = View.GONE;
    }
}