package me.ameriod.trivia.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup

interface TriviaAdapterItem {

    fun createViewHolder(inflater: LayoutInflater, parent: ViewGroup?, attachToRoot: Boolean): TriviaBaseViewHolder<*>

    fun getViewType(): Int

    fun getRecyclerItemId(): String
}