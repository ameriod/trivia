package me.ameriod.trivia.ui.play

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.play_controller.view.*
import me.ameriod.lib.mvp.view.MvpController
import me.ameriod.trivia.R
import me.ameriod.trivia.api.response.OtCategory
import me.ameriod.trivia.api.response.OtDifficulty
import me.ameriod.trivia.ui.filter.FilterContract
import me.ameriod.trivia.ui.filter.FilterPresenter
import me.ameriod.trivia.ui.quiz.Quiz
import me.ameriod.trivia.ui.quiz.QuizActivity

class PlayController(args: Bundle) : MvpController<FilterContract.View, FilterContract.Presenter>(args),
        FilterContract.View, View.OnClickListener {


    override fun inflateView(inflater: LayoutInflater, container: ViewGroup): View {
        val v = inflater.inflate(R.layout.play_controller, container, false)
        v.setOnClickListener(this)
        return v
    }

    override fun createPresenter() = FilterPresenter.newInstance(activity!!.applicationContext)

    override fun displayError(error: String) {
        com.google.android.material.snackbar.Snackbar.make(view!!, error, com.google.android.material.snackbar.Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.filter_retry) { _ ->
                    getPresenter().getQuestions()
                }.show()
    }

    override fun showProgress(show: Boolean) {
        view?.isEnabled = !show
        view?.playLoading?.visibility = if (show) View.VISIBLE else View.GONE
    }

    override fun setQuiz(quiz: Quiz) {
        if (quiz.isQuizDone()) {
            com.google.android.material.snackbar.Snackbar.make(view!!, R.string.filter_no_more, com.google.android.material.snackbar.Snackbar.LENGTH_SHORT).show()
        } else {
            startActivity(QuizActivity.getLaunchIntent(activity!!, quiz))
        }
    }

    override fun onClick(v: View?) {
        getPresenter().getQuestions()
    }

    override fun setQuestionCount(count: String) {
        // no op
    }

    override fun setCategories(categories: List<OtCategory>, selectedItem: OtCategory) {
        // no op
    }

    override fun setDifficulties(difficulties: List<OtDifficulty>, selectedItem: OtDifficulty) {
        // no op
    }

    companion object {

        fun newInstance() = PlayController(Bundle.EMPTY)
    }
}