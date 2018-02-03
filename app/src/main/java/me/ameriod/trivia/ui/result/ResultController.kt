package me.ameriod.trivia.ui.result

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bluelinelabs.conductor.Controller
import me.ameriod.trivia.R
import me.ameriod.trivia.ui.quiz.Quiz

class ResultController(args: Bundle) : Controller(args) {

    val result: Quiz = args.getParcelable(RESULT)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        val v = inflater.inflate(R.layout.controller_result, container, false)

        return v
    }

    companion object {
        private const val RESULT = "result"

        @JvmStatic
        fun newInstance(finished: Quiz): ResultController {
            val args = Bundle()
            args.putParcelable(RESULT, finished)
            return ResultController(args)
        }
    }
}