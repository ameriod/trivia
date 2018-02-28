package me.ameriod.trivia.ui.adapter

import android.support.v7.widget.RecyclerView
import android.view.View

abstract class TriviaBaseViewHolder<in T : TriviaAdapterItem>(view: View) : RecyclerView.ViewHolder(view) {

    var clickListener: TriviaBaseAdapter.OnItemClickListener? = null
    var longClickListener: TriviaBaseAdapter.OnItemLongClickListener? = null

    @Suppress("UNCHECKED_CAST")
    fun bindView(item: TriviaAdapterItem) {
        bindItem(item as T)
    }

    protected abstract fun bindItem(item: T)

    open fun unbindView() {
        // no op, override to impl
    }
}