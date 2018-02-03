package me.ameriod.trivia.ui.quiz.question

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.bluelinelabs.conductor.Controller
import kotlinx.android.synthetic.main.controller_question.view.*
import me.ameriod.trivia.R
import me.ameriod.trivia.api.response.Question
import me.ameriod.trivia.ui.adapter.TriviaBaseAdapter
import me.ameriod.trivia.ui.adapter.TriviaBaseViewHolder

class QuestionController(args: Bundle) : Controller(args), View.OnClickListener,
        TriviaBaseAdapter.OnItemClickListener {

    private val question: Question = args.getParcelable(QUESTION)
    private val adapter: TriviaBaseAdapter<Answer> by lazy {
        TriviaBaseAdapter<Answer>(activity!!, this)
    }
    private val answers: List<Answer> by lazy {
        question.answers
    }
    private var selectedAnswer: Answer = Answer.EMPTY

    private val last = args.getBoolean(LAST)
    private var listener: OnQuestionAnsweredListener? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        val v = inflater.inflate(R.layout.controller_question, container, false)
        v.questionTv.text = Html.fromHtml(question.question)


        v.questionBtnNext.setText(if (last) R.string.questions_btn_finish else R.string.questions_btn_next_question)
        v.questionBtnNext.setOnClickListener(this)

        v.questionAnswersRecycler.layoutManager = LinearLayoutManager(v.context)
        v.questionAnswersRecycler.adapter = adapter
        adapter.setSingleSelected(selectedAnswer)
        adapter.setItems(answers)

        return v
    }

    override fun onSaveViewState(view: View, outState: Bundle) {
        super.onSaveViewState(view, outState)
        outState.putParcelable(OUT_SELECTED_ANSWER, selectedAnswer)
    }

    override fun onRestoreViewState(view: View, savedViewState: Bundle) {
        super.onRestoreViewState(view, savedViewState)
        selectedAnswer = savedViewState.getParcelable(OUT_SELECTED_ANSWER)
    }

    override fun onAttach(view: View) {
        super.onAttach(view)
        listener = activity as OnQuestionAnsweredListener
    }

    override fun onDetach(view: View) {
        super.onDetach(view)
        listener = null
    }

    override fun onClick(v: View) {
        val view = view!!
        when (v) {
            view.questionBtnNext -> {
                if (selectedAnswer == Answer.EMPTY) {
                    Toast.makeText(view.context, R.string.questions_error_required, Toast.LENGTH_SHORT).show()
                } else {
                    listener?.onQuestionAnswered(selectedAnswer, question)
                }
            }
        }
    }

    override fun onItemClicked(vh: TriviaBaseViewHolder<*>, position: Int) {
        val answer = adapter.getItem(position)
        answers.forEach { item ->
            item.selected = answer.display == item.display
        }
        selectedAnswer = answer
        adapter.setSingleSelected(answer)
    }

    interface OnQuestionAnsweredListener {
        fun onQuestionAnswered(answer: Answer, question: Question)
    }

    companion object {

        private const val QUESTION = "question"
        private const val LAST = "last"

        private const val OUT_SELECTED_ANSWER = "out_selected_answer"

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