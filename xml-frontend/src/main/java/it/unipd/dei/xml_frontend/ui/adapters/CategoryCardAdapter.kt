package it.unipd.dei.xml_frontend.ui.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import it.unipd.dei.common_backend.models.Category
import it.unipd.dei.common_backend.viewModels.CategoryViewModel
import it.unipd.dei.xml_frontend.R
import it.unipd.dei.xml_frontend.ui.view.holder.CategoryViewHolder


class CategoryCardAdapter(
    private var categories: List<Category>,
    private val parentFragmentManager: FragmentManager,
    private val categoryViewModel: CategoryViewModel,
) :
    RecyclerView.Adapter<CategoryCardAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_category_card_with_button, parent, false)
        return ViewHolder(CategoryViewHolder(view, parentFragmentManager))
    }

    override fun getItemCount(): Int = categories.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val category = categories[position]
        holder.bind(category, categoryViewModel)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateCategories(categories: List<Category>) {
        this.categories = categories
        notifyDataSetChanged()
    }

    class ViewHolder(private val categoryViewHolder: CategoryViewHolder) :
        RecyclerView.ViewHolder(categoryViewHolder.getItemView()) {

        fun bind(
            category: Category,
            categoryViewModel: CategoryViewModel,
        ) {
            categoryViewHolder.bindWithButton(
                category,
                categoryViewModel
            )
        }
    }
}