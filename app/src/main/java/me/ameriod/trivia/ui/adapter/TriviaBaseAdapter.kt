package me.ameriod.trivia.ui.adapter

import android.content.Context
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup

abstract class TriviaBaseAdapter<VH : TriviaBaseViewHolder<T>, T : TriviaAdapterItem>(protected val context: Context) :
        RecyclerView.Adapter<VH>() {

    private val inflater = LayoutInflater.from(context)
    private var items = listOf<T>()

    override fun getItemViewType(position: Int): Int = items[position].getViewType()

    @Suppress("UNCHECKED_CAST")
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): VH =
            items.first { item -> item.getViewType() == viewType }
                    .createViewHolder(inflater, parent, viewType) as VH

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bindView(items[position])
    }

    fun getItem(position: Int): T = items[position]

    fun setItems(newItems: List<T>) {
        // copy the old items
        val oldItems = items.toList()
        // set the new
        items = newItems
        // calculate the diff
        DiffUtil.calculateDiff(DiffCallback(newItems, oldItems))
                .dispatchUpdatesTo(this)
    }

    private inner class DiffCallback(private val newItems: List<T>,
                                     private val oldItems: List<T>) : DiffUtil.Callback() {

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val new = newItems[newItemPosition]
            val old = oldItems[oldItemPosition]
            // check the view type
            return new.getViewType() == old.getViewType() &&
                    // check the item id
                    new.getRecylcerItemId() == old.getRecylcerItemId()
        }

        override fun getOldListSize(): Int = oldItems.size

        override fun getNewListSize(): Int = newItems.size

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
                newItems[newItemPosition] == oldItems[oldItemPosition]
    }
}