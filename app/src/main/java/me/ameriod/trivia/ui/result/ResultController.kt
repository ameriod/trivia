package me.ameriod.trivia.ui.result

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bluelinelabs.conductor.Controller
import me.ameriod.trivia.R

class ResultController(args: Bundle) : Controller(args) {

    val result: QuizResult = args.getParcelable(RESULT)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        val v = inflater.inflate(R.layout.controller_result, container, false)

        return v
    }

    companion object {
        private const val RESULT = "result"

        @JvmStatic
        fun newInstance(result: QuizResult): ResultController {
            val args = Bundle()
            args.putParcelable(RESULT, result)
            return ResultController(args)
        }
    }
}