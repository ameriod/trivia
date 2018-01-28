package me.ameriod.trivia.ui.categories

import android.content.Context
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import me.ameriod.trivia.R
import me.ameriod.trivia.api.response.Category

class CategoriesAdapter(context: Context,
                        private val clickListener: OnItemClickListener) : RecyclerView.Adapter<CategoryViewHolder>() {

    interface OnItemClickListener {
        fun onItemClicked(view: View, position: Int)
    }

    private val layoutInflater = LayoutInflater.from(context)
    private var items = listOf<Category>()

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): CategoryViewHolder =
            CategoryViewHolder(layoutInflater.inflate(R.layout.category_item, parent, false), clickListener)

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: CategoryViewHolder?, position: Int) {
        holder?.bind(items[position])
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