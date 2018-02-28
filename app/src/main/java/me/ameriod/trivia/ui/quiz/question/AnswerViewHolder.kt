package me.ameriod.trivia.ui.quiz.question

import android.graphics.Color
import android.support.v4.content.ContextCompat
import android.text.Html
import android.view.View
import kotlinx.android.synthetic.main.answer_item.view.*
import me.ameriod.trivia.R
import me.ameriod.trivia.ui.adapter.TriviaBaseViewHolder

class AnswerViewHolder(view: View) : TriviaBaseViewHolder<Answer>(view), View.OnClickListener {

    override fun bindItem(item: Answer) {
        itemView.answerTv.text = Html.fromHtml(item.display)
        if (item.selected || item.showCorrect) {
            itemView.answerTv.setTextColor(Color.WHITE)
            itemView.setBackgroundColor(ContextCompat.getColor(itemView.context, if (item.correct)
                R.color.colorCorrectAnswer else R.color.colorIncorrectAnswer))

        } else {
            itemView.answerTv.setTextColor(Color.BLACK)
            itemView.setBackgroundColor(Color.WHITE)
        }
        itemView.answerBtn.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        clickListener?.onItemClicked(this, adapterPosition)
    }
}