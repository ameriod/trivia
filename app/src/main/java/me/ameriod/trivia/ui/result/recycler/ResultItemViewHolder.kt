package me.ameriod.trivia.ui.result.recycler

import androidx.core.content.ContextCompat
import android.text.Html
import android.view.View
import androidx.core.text.parseAsHtml
import kotlinx.android.synthetic.main.result_item.view.*
import me.ameriod.trivia.R
import me.ameriod.trivia.ui.adapter.TriviaBaseViewHolder
import me.ameriod.trivia.ui.result.ResultItem

class ResultItemViewHolder(view: View) : TriviaBaseViewHolder<ResultItem>(view) {

    private val context = itemView.context

    override fun bindItem(item: ResultItem) {
        itemView.resultTvQuestion.text = item.questionText.parseAsHtml()
        itemView.resultTvSelectedAnswer.text = item.selectedAnswer.parseAsHtml()
        itemView.resultTvCorrectAnswer.text = item.correctAnswers.parseAsHtml()

        if (item.isCorrect) {
            itemView.resultTvSelectedAnswer.setTextColor(ContextCompat.getColor(context, R.color.correct_answer))
            itemView.resultTvCorrectAnswer.visibility = View.GONE
        } else {
            itemView.resultTvSelectedAnswer.setTextColor(ContextCompat.getColor(context, R.color.incorrect_answer))
            itemView.resultTvSelectedAnswer.visibility = View.VISIBLE
        }
    }
}