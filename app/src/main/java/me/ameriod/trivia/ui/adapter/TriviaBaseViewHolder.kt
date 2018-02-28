package me.ameriod.trivia.ui.adapter

import android.support.v7.widget.RecyclerView
import android.view.View

abstract class TriviaBaseViewHolder<in T : TriviaAdapterItem>(view: View) : RecyclerView.ViewHolder(view) {

    protected var clickListener: TriviaBaseAdapter.OnItemClickListener? = null
    protected var longClickListener: TriviaBaseAdapter.OnItemLongClickListener? = null

    fun setItemClickListener(itemClickListener: TriviaBaseAdapter.OnItemClickListener?) {
        this.clickListener = itemClickListener
    }

    fun setItemLongClickListener(itemLongClickListener: TriviaBaseAdapter.OnItemLongClickListener?) {
        this.longClickListener = itemLongClickListener
    }

    @Suppress("UNCHECKED_CAST")
    fun bindView(item: TriviaAdapterItem) {
        bindItem(item as T)
    }

    protected abstract fun bindItem(item: T)

    open fun unbindView() {
        this.clickListener = null
        this.longClickListener = null
    }
}