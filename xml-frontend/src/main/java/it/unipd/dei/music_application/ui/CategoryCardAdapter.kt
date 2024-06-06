package it.unipd.dei.music_application.ui

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import it.unipd.dei.music_application.R
import it.unipd.dei.music_application.interfaces.OnItemClickListener
import it.unipd.dei.music_application.models.Category


class CategoryCardAdapter(
    private var categories: List<Category>,
    private val clickListener: OnItemClickListener
) :
    RecyclerView.Adapter<CategoryCardAdapter.CategoryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_category_card_with_button_and_divider, parent, false)
        return CategoryViewHolder(view)
    }

    override fun getItemCount(): Int = categories.size

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val category = categories[position]
        holder.bind(category, clickListener)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateCategories(categories: List<Category>) {
        this.categories = categories
        notifyDataSetChanged()
    }

    class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val categoryIdentifier: TextView =
            itemView.findViewById(R.id.category_card_identifier)
        private val categoryImageButton: ImageButton =
            itemView.findViewById(R.id.view_category_button)

        fun bind(category: Category, clickListener: OnItemClickListener) {
            categoryIdentifier.text = category.identifier
            categoryImageButton.setOnClickListener {
                clickListener.onItemClick(category)
            }
        }
    }
}