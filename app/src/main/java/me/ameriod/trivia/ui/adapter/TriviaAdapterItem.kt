package me.ameriod.trivia.ui.adapter

import android.os.Parcelable
import android.view.LayoutInflater
import android.view.ViewGroup
import kotlinx.android.parcel.Parcelize

interface TriviaAdapterItem : Parcelable {

    fun createViewHolder(inflater: LayoutInflater, parent: ViewGroup?, attachToRoot: Boolean): TriviaBaseViewHolder<*>

    fun getViewType(): Int

    fun getRecyclerItemId(): String

}