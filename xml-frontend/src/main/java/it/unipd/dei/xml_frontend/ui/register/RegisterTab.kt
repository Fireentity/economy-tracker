package it.unipd.dei.xml_frontend.ui.register

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import it.unipd.dei.common_backend.view.MovementWithCategoryViewModel
import it.unipd.dei.xml_frontend.ui.MovementCardAdapter

class RegisterTab(
    private val viewModel: MovementWithCategoryViewModel,
    private val recyclerView: RecyclerView,
    private val movementCardAdapter: MovementCardAdapter,
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
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0 && !loading && recyclerView.visibility == View.VISIBLE) {
                    super.onScrolled(recyclerView, dx, dy)
                    val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                    val totalItemCount = layoutManager.itemCount
                    val lastVisibleItem = layoutManager.findLastVisibleItemPosition()
                    if (totalItemCount <= (lastVisibleItem + VISIBLE_THRESHOLD)) {
                        loading = true
                        viewModel.loadSomeMovementsByCategory{
                            loading = false
                        }
                    }
                }
            }
        })
        viewModel.getMovements()
    }

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