package me.ameriod.trivia.ui.quiz.question

import android.content.Context
import me.ameriod.trivia.ui.adapter.TriviaBaseAdapter

class AnswerAdapter(
        context: Context,
        listener: TriviaBaseAdapter.OnItemClickListener
) : TriviaBaseAdapter<Answer>(context, listener) {

    fun showCorrectAnswer() {
        val items = getItems()
        val correct = items.findLast { answer ->
            answer.correct
        }!!
        if (correct != getSelectedItem()) {
            correct.showCorrect = true
            notifyItemChanged(items.indexOf(correct))
        }
    }

}
