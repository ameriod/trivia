package me.ameriod.trivia.ui.quiz.question

import android.support.v4.content.ContextCompat
import android.text.Html
import android.view.View
import kotlinx.android.synthetic.main.answer_item.view.*
import me.ameriod.trivia.R
import me.ameriod.trivia.ui.adapter.TriviaBaseViewHolder

class AnswerViewHolder(view: View) : TriviaBaseViewHolder<Answer>(view), View.OnClickListener {

    private val context = itemView.context

    override fun bindItem(item: Answer) {
        itemView.answerTv.text = Html.fromHtml(item.display)
        if (item.selected || item.showCorrect) {
            itemView.answerTv.setTextColor(ContextCompat.getColor(context, R.color.text_color_inverse))
            itemView.setBackgroundColor(ContextCompat.getColor(context, if (item.correct)
                R.color.correct_answer else R.color.incorrect_answer))

        } else {
            itemView.answerTv.setTextColor(ContextCompat.getColor(context, R.color.text_color))
            itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.question_card_color))
        }
        itemView.answerBtn.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        clickListener?.onItemClicked(this, adapterPosition)
    }
}