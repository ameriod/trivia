package me.ameriod.trivia.ui.result.recycler

import android.os.Parcel
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.ViewGroup
import me.ameriod.trivia.R
import me.ameriod.trivia.ui.adapter.TriviaAdapterItem
import me.ameriod.trivia.ui.adapter.TriviaBaseViewHolder

data class ResultGraphItem(val total: Int,
                           val correct: Int,
                           val incorrect: Int) : TriviaAdapterItem, Parcelable {

    override fun createViewHolder(inflater: LayoutInflater, parent: ViewGroup?, attachToRoot: Boolean): TriviaBaseViewHolder<*> =
            ResultGraphViewHolder(inflater.inflate(R.layout.result_item_graph, parent, attachToRoot))

    override fun getViewType() = R.layout.result_item_graph

    override fun getRecyclerItemId() = ""

    constructor(source: Parcel) : this(
            source.readInt(),
            source.readInt(),
            source.readInt()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeInt(total)
        writeInt(correct)
        writeInt(incorrect)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<ResultGraphItem> = object : Parcelable.Creator<ResultGraphItem> {
            override fun createFromParcel(source: Parcel): ResultGraphItem = ResultGraphItem(source)
            override fun newArray(size: Int): Array<ResultGraphItem?> = arrayOfNulls(size)
        }
    }
}