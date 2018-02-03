package me.ameriod.trivia.ui.quiz.question

import android.text.Html
import android.view.View
import kotlinx.android.synthetic.main.answer_item.view.*
import me.ameriod.trivia.ui.adapter.TriviaBaseViewHolder

class AnswerViewHolder(view: View) : TriviaBaseViewHolder<QuizAnswer>(view), View.OnClickListener {

    override fun bindItem(item: QuizAnswer) {
        itemView.answerTv.text = Html.fromHtml(item.display)
        itemView.answerBtn.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        clickListener?.onItemClicked(this, adapterPosition)
    }
}