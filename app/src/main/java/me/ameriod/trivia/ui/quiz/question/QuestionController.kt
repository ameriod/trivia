package me.ameriod.trivia.ui.quiz.question

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bluelinelabs.conductor.Controller
import kotlinx.android.synthetic.main.controller_question.view.*
import me.ameriod.trivia.R
import me.ameriod.trivia.api.response.Question
import me.ameriod.trivia.ui.adapter.TriviaBaseAdapter
import me.ameriod.trivia.ui.adapter.TriviaBaseViewHolder

class QuestionController(args: Bundle) : Controller(args), View.OnClickListener,
        TriviaBaseAdapter.OnItemClickListener {

    private val question: Question = args.getParcelable(QUESTION)
    private val adapter: TriviaBaseAdapter<QuizAnswer> by lazy {
        TriviaBaseAdapter<QuizAnswer>(activity!!, this)
    }
    private val answers: List<QuizAnswer> by lazy {
        question.answers.map { answer ->
            QuizAnswer(answer, question.isCorrect(answer), false)
        }
    }

    private val last = args.getBoolean(LAST)
    private var listener: OnQuestionAnsweredListener? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        val v = inflater.inflate(R.layout.controller_question, container, false)
        v.questionTv.text = Html.fromHtml(question.question)


        v.questionBtnNext.setText(if (last) R.string.questions_btn_finish else R.string.questions_btn_next_question)
        v.questionBtnNext.setOnClickListener(this)

        v.questionAnswersRecycler.layoutManager = LinearLayoutManager(v.context)
        v.questionAnswersRecycler.adapter = adapter
        adapter.setItems(answers)

        return v
    }

    override fun onSaveViewState(view: View, outState: Bundle) {
        super.onSaveViewState(view, outState)
    }

    override fun onRestoreViewState(view: View, savedViewState: Bundle) {
        super.onRestoreViewState(view, savedViewState)
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
                // val checkedPosition = view.questionGroup.checkedRadioButtonId
//                if (checkedPosition >= 0) {
//                    listener?.onQuestionAnswered(answers[checkedPosition], question)
//                } else {
//                    Toast.makeText(view.context, R.string.questions_error_required, Toast.LENGTH_SHORT).show()
//                }
            }
        }
    }

    override fun onItemClicked(vh: TriviaBaseViewHolder<*>, position: Int) {
        val answer = adapter.getItem(position)
        adapter.setSingleSelected(answer)
    }

    interface OnQuestionAnsweredListener {
        fun onQuestionAnswered(answer: String, question: Question)
    }

    companion object {

        private const val QUESTION = "question"
        private const val LAST = "last"

        private const val OUT_CHECKED_POSITION = "out_checked_position"

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