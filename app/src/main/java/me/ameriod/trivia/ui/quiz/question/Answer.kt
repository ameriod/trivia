package me.ameriod.trivia.ui.quiz.question

import android.os.Parcel
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.ViewGroup
import me.ameriod.trivia.R
import me.ameriod.trivia.ui.adapter.TriviaAdapterItem
import me.ameriod.trivia.ui.adapter.TriviaBaseViewHolder

data class Answer(val display: String,
                  val correct: Boolean,
                  var selected: Boolean,
                  var showCorrect: Boolean = false) : Parcelable, TriviaAdapterItem {

    constructor(source: Parcel) : this(
            source.readString(),
            1 == source.readInt(),
            1 == source.readInt()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(display)
        writeInt((if (correct) 1 else 0))
        writeInt((if (selected) 1 else 0))
    }

    companion object {
        val EMPTY = Answer("", false, false)

        @JvmField
        val CREATOR: Parcelable.Creator<Answer> = object : Parcelable.Creator<Answer> {
            override fun createFromParcel(source: Parcel): Answer = Answer(source)
            override fun newArray(size: Int): Array<Answer?> = arrayOfNulls(size)
        }
    }

    override fun createViewHolder(inflater: LayoutInflater, parent: ViewGroup?, attachToRoot: Boolean): TriviaBaseViewHolder<*> {
        return AnswerViewHolder(inflater.inflate(R.layout.answer_item, parent, attachToRoot))
    }

    override fun getViewType(): Int = R.layout.answer_item

    override fun getRecyclerItemId(): String = display
}