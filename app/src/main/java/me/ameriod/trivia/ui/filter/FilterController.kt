package me.ameriod.trivia.ui.filter

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bluelinelabs.conductor.Controller
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.controller_filter.view.*
import me.ameriod.trivia.R
import me.ameriod.trivia.api.TriviaRepository
import me.ameriod.trivia.ui.quiz.QuizActivity
import timber.log.Timber

class FilterController(args: Bundle) : Controller(args), View.OnClickListener {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        val v = inflater.inflate(R.layout.controller_filter, container, false)
        v.filterBtnStart.setOnClickListener(this)
        return v
    }

    override fun onClick(v: View) {
        val view = view!!
        when (v) {
            view.filterBtnStart -> startQuestions(v)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE) {
            // TODO clear filters
        }
    }

    private fun startQuestions(view: View) {
        TriviaRepository().getQuestions(QuizFilter(10, null, null))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response ->
                    startActivityForResult(QuizActivity.getLaunchIntent(view.context, response.results), REQUEST_CODE)
                }, { throwable ->
                    Timber.e(throwable, "Error")
                })
    }

    companion object {
        const val REQUEST_CODE = 1000

        @JvmStatic
        fun newInstance() = FilterController(Bundle.EMPTY)
    }
}