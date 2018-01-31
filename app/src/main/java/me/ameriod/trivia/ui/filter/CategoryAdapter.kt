package me.ameriod.trivia.ui.filter

import android.content.Context
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import me.ameriod.trivia.R
import me.ameriod.trivia.api.response.Category

class CategoryAdapter(context: Context,
                      private val clickListener: OnItemClickListener) : RecyclerView.Adapter<CategoryViewHolder>() {

    interface OnItemClickListener {
        fun onItemClicked(view: View, position: Int)
    }

    private val layoutInflater = LayoutInflater.from(context)
    private var items = listOf<Category>()
    private var selectedCategory: Category? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder =
            CategoryViewHolder(layoutInflater.inflate(R.layout.category_item, parent, false), clickListener)

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val category = items[position]
        holder.bind(category, category == selectedCategory)
    }

    fun getItem(position: Int) = items[position]

    fun setItems(newItems: List<Category>) {
        // copy the old items
        val oldItems = items.toList()
        // set the new
        items = newItems
        // calculate the diff
        DiffUtil.calculateDiff(DiffCallback(newItems, oldItems))
                .dispatchUpdatesTo(this)
    }

    fun setSelectedCategory(newCategory: Category) {
        if (items.isNotEmpty() || selectedCategory != newCategory) {
            val oldPosition = items.indexOf(selectedCategory)
            val newPosition = items.indexOf(newCategory)
            // set the new category
            this.selectedCategory = newCategory
            if (oldPosition >= 0) {
                notifyItemChanged(oldPosition)
            }
            if (newPosition >= 0) {
                notifyItemChanged(newPosition)
            }
        }
        if (selectedCategory == null) {
            this.selectedCategory = newCategory
        }
    }

    private inner class DiffCallback(private val newItems: List<Category>,
                                     private val oldItems: List<Category>) : DiffUtil.Callback() {

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
                newItems[newItemPosition].id == oldItems[oldItemPosition].id

        override fun getOldListSize(): Int = oldItems.size

        override fun getNewListSize(): Int = newItems.size

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
                newItems[newItemPosition] == oldItems[oldItemPosition]
    }
}