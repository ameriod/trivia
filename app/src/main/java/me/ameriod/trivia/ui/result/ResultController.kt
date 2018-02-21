package me.ameriod.trivia.ui.result

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bluelinelabs.conductor.Controller
import kotlinx.android.synthetic.main.controller_result.view.*
import me.ameriod.trivia.R

class ResultController(args: Bundle) : Controller(args) {

    val result: Result = args.getParcelable(RESULT)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        val v = inflater.inflate(R.layout.controller_result, container, false)
        v.resultBtnDone.setOnClickListener { _ -> activity?.finish() }
        return v
    }

    companion object {
        private const val RESULT = "result"

        @JvmStatic
        fun newInstance(result: Result): ResultController {
            val args = Bundle()
            args.putParcelable(RESULT, result)
            return ResultController(args)
        }
    }
}