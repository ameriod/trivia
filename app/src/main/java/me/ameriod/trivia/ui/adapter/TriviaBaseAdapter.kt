package me.ameriod.trivia.ui.adapter

import android.content.Context
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup

abstract class TriviaBaseAdapter<T : TriviaAdapterItem>(protected val context: Context,
                                                        private val onItemClickListener: OnItemClickListener? = null,
                                                        private val onItemLongClickListener: TriviaBaseAdapter.OnItemLongClickListener? = null) :
        RecyclerView.Adapter<TriviaBaseViewHolder<T>>() {

    interface OnItemClickListener {
        fun onItemClicked(vh: TriviaBaseViewHolder<*>, position: Int)
    }

    interface OnItemLongClickListener {
        fun onItemLongClicked(vh: TriviaBaseViewHolder<*>, position: Int)
    }

    private val inflater = LayoutInflater.from(context)
    private var items = listOf<T>()

    private var selectedItem: T? = null

    override fun getItemViewType(position: Int): Int = items[position].getViewType()

    @Suppress("UNCHECKED_CAST")
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): TriviaBaseViewHolder<T> =
            items.first { item -> item.getViewType() == viewType }
                    .createViewHolder(inflater, parent, viewType) as TriviaBaseViewHolder<T>

    override fun onBindViewHolder(holder: TriviaBaseViewHolder<T>, position: Int) {
        holder.setItemClickListener(onItemClickListener)
        holder.setItemLongClickListener(onItemLongClickListener)
        holder.bindView(items[position])
    }

    override fun onViewDetachedFromWindow(holder: TriviaBaseViewHolder<T>) {
        super.onViewDetachedFromWindow(holder)
        holder.unbindView()
    }

    override fun getItemCount(): Int = items.size

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

    fun setSingleSelected(newSelectedItem: T) {
        when {
            items.isEmpty() -> return
            selectedItem == null -> {
                this.selectedItem = newSelectedItem
                notifyItemChanged(items.indexOf(newSelectedItem))
            }
            else -> {
                val oldPosition = items.indexOf(selectedItem!!)
                val newPosition = items.indexOf(newSelectedItem)
                // set the new category
                this.selectedItem = newSelectedItem
                if (oldPosition >= 0) {
                    notifyItemChanged(oldPosition)
                }
                if (newPosition >= 0) {
                    notifyItemChanged(newPosition)
                }
            }
        }
    }

    private inner class DiffCallback(private val newItems: List<T>,
                                     private val oldItems: List<T>) : DiffUtil.Callback() {

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val new = newItems[newItemPosition]
            val old = oldItems[oldItemPosition]
            // check the view type
            return new.getViewType() == old.getViewType() &&
                    // check the item id
                    new.getRecyclerItemId() == old.getRecyclerItemId()
        }

        override fun getOldListSize(): Int = oldItems.size

        override fun getNewListSize(): Int = newItems.size

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
                newItems[newItemPosition] == oldItems[oldItemPosition]
    }
}