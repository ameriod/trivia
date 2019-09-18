package me.ameriod.trivia.ui.result.recycler

import android.os.Parcelable
import android.view.LayoutInflater
import android.view.ViewGroup
import kotlinx.android.parcel.Parcelize
import me.ameriod.trivia.R
import me.ameriod.trivia.ui.adapter.TriviaAdapterItem
import me.ameriod.trivia.ui.adapter.TriviaBaseViewHolder

@Parcelize
data class ResultGraphItem(
        val total: Int,
        val correct: Int,
        val incorrect: Int
) : TriviaAdapterItem, Parcelable {

    override fun createViewHolder(inflater: LayoutInflater, parent: ViewGroup?, attachToRoot: Boolean): TriviaBaseViewHolder<*> =
            ResultGraphViewHolder(inflater.inflate(R.layout.result_item_graph, parent, attachToRoot))

    override fun getViewType() = R.layout.result_item_graph

    override fun getRecyclerItemId() = ""

}