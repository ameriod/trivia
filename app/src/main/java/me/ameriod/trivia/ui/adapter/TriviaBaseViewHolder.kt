package me.ameriod.trivia.ui.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView

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