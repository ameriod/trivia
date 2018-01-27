package me.ameriod.trivia.ui.filter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bluelinelabs.conductor.Controller
import kotlinx.android.synthetic.main.controller_filter.view.*
import me.ameriod.trivia.R

class FilterController(args: Bundle) : Controller(args), View.OnClickListener{

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
      val v = inflater.inflate(R.layout.controller_filter, container, false)
        v.filterBtnStart.setOnClickListener(this)
        return v
    }

    override fun onClick(v: View) {
        val view = view!!
        when(v) {
            view.filterBtnStart -> startQuestions()
        }
    }

    private fun startQuestions() {

    }
    companion object {
        @JvmStatic
        fun newInstance() = FilterController(Bundle.EMPTY)
    }
}