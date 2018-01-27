package me.ameriod.trivia.ui.question

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bluelinelabs.conductor.Controller
import me.ameriod.trivia.R
import me.ameriod.trivia.api.response.Question

class QuestionController(args: Bundle) : Controller(args) {

    private val question: Question = args.getParcelable(QUESTION)


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        val v = inflater.inflate(R.layout.controller_question, container, false)

        return v
    }

    companion object {

        private const val QUESTION = "question"

        @JvmStatic
        fun newInstance(question: Question): QuestionController {
            val args = Bundle()
            args.putParcelable(QUESTION, question)
            return QuestionController(args)
        }
    }
}