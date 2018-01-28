package me.ameriod.trivia.ui.question

import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioGroup
import android.widget.Toast
import com.bluelinelabs.conductor.Controller
import kotlinx.android.synthetic.main.controller_question.view.*
import me.ameriod.trivia.R
import me.ameriod.trivia.api.response.Question

class QuestionController(args: Bundle) : Controller(args), View.OnClickListener {

    private val question: Question = args.getParcelable(QUESTION)
    private val answers: List<String> by lazy {
        question.answers
    }
    private val last = args.getBoolean(LAST)
    private var listener: OnQuestionAnsweredListener? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        val v = inflater.inflate(R.layout.controller_question, container, false)
        v.questionTv.text = Html.fromHtml(question.question)

        val layoutParams = RadioGroup.LayoutParams(RadioGroup.LayoutParams.MATCH_PARENT,
                RadioGroup.LayoutParams.WRAP_CONTENT)

        for (i in answers.indices) {
            val answer = answers[i]
            val radio = QuestionRadioButton(v.context)
            // set the id to the index
            radio.id = i
            radio.text = Html.fromHtml(answer)
            v.questionGroup.addView(radio, layoutParams)
        }

        v.questionBtnNext.setText(if (last) R.string.questions_btn_finish else R.string.questions_btn_next_question)
        v.questionBtnNext.setOnClickListener(this)

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

    override fun onClick(v: View) {
        val view = view!!
        when (v) {
            view.questionBtnNext -> onNextClicked(view)
        }
    }

    private fun onNextClicked(view: View) {
        val checkedPosition = view.questionGroup.checkedRadioButtonId
        if (checkedPosition >= 0) {
            listener?.onQuestionAnswered(answers[checkedPosition], question)
        } else {
            Toast.makeText(view.context, R.string.questions_error_required, Toast.LENGTH_SHORT).show()
        }
    }

    interface OnQuestionAnsweredListener {
        fun onQuestionAnswered(answer: String, question: Question)
    }

    companion object {

        private const val QUESTION = "question"
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