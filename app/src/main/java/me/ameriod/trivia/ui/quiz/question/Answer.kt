package me.ameriod.trivia.ui.quiz.question

import android.os.Parcelable
import android.view.LayoutInflater
import android.view.ViewGroup
import kotlinx.android.parcel.Parcelize
import me.ameriod.trivia.R
import me.ameriod.trivia.ui.adapter.TriviaAdapterItem
import me.ameriod.trivia.ui.adapter.TriviaBaseViewHolder

@Parcelize
data class Answer(
        val display: String,
        val correct: Boolean,
        var selected: Boolean,
        var showCorrect: Boolean = false
) : Parcelable, TriviaAdapterItem {


    companion object {

        val EMPTY = Answer("", correct = false, selected = false)

    }

    override fun createViewHolder(inflater: LayoutInflater, parent: ViewGroup?, attachToRoot: Boolean): TriviaBaseViewHolder<*> {
        return AnswerViewHolder(inflater.inflate(R.layout.answer_item, parent, attachToRoot))
    }

    override fun getViewType(): Int = R.layout.answer_item

    override fun getRecyclerItemId(): String = display
}