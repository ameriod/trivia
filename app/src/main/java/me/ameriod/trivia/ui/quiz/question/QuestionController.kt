package me.ameriod.trivia.ui.quiz.question

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.htmlEncode
import androidx.core.text.parseAsHtml
import com.bluelinelabs.conductor.Controller
import kotlinx.android.synthetic.main.controller_question.view.*
import me.ameriod.trivia.R
import me.ameriod.trivia.ui.adapter.TriviaBaseAdapter
import me.ameriod.trivia.ui.adapter.TriviaBaseViewHolder

class QuestionController(args: Bundle) : Controller(args), TriviaBaseAdapter.OnItemClickListener {

    private val question: Question = args.getParcelable(QUESTION) ?: throw IllegalArgumentException("Error need to pass in a question")
    private val adapter: AnswerAdapter by lazy {
        AnswerAdapter(activity!!, this)
    }
    private val answers: List<Answer> by lazy {
        question.answers
    }
    private var selectedAnswer: Answer = Answer.EMPTY

    private val last = args.getBoolean(LAST)
    private var listener: OnQuestionAnsweredListener? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        val v = inflater.inflate(R.layout.controller_question, container, false)
        v.questionTv.text = question.text.parseAsHtml()


        v.questionAnswersRecycler.layoutManager = LinearLayoutManager(v.context)
        v.questionAnswersRecycler.adapter = adapter
        adapter.setSingleSelected(selectedAnswer)
        adapter.setItems(answers)

        return v
    }

    override fun onAttach(view: View) {
        super.onAttach(view)
        listener = activity as OnQuestionAnsweredListener
    }

    override fun onDetach(view: View) {
        super.onDetach(view)
        listener = null
    }

    override fun onItemClicked(vh: TriviaBaseViewHolder<*>, position: Int) {
        // disable clicking if already answered
        if (selectedAnswer != Answer.EMPTY) {
            return
        }

        val answer = adapter.getItem(position)
        answer.selected = true
        selectedAnswer = answer
        adapter.setSingleSelected(answer)
        adapter.showCorrectAnswer()

        // move on to the next question, delay so user can see what they selected
        vh.itemView.postDelayed({
            listener?.onQuestionAnswered(selectedAnswer, question)
        }, ANSWER_MOVE_DELAY)
    }

    interface OnQuestionAnsweredListener {
        fun onQuestionAnswered(answer: Answer, question: Question)
    }

    companion object {

        private const val ANSWER_MOVE_DELAY = 1000L

        private const val QUESTION = "text"
        private const val LAST = "last"

        @JvmStatic
        fun newInstance(question: Question,
                        last: Boolean): QuestionController {
            val args = Bundle()
            args.putParcelable(QUESTION, question)
            args.putBoolean(LAST, last)
            return QuestionController(args)
        }
    }
}