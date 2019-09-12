package me.ameriod.trivia.ui.play

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.google.android.material.snackbar.Snackbar
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.play_controller.view.*
import me.ameriod.trivia.R
import me.ameriod.trivia.mvvm.MvvmController
import me.ameriod.trivia.mvvm.viewModel
import me.ameriod.trivia.ui.filter.FilterViewModel
import me.ameriod.trivia.ui.quiz.Quiz
import me.ameriod.trivia.ui.quiz.QuizActivity

class PlayController(args: Bundle) : MvvmController(args), View.OnClickListener {

    private val viewModel: FilterViewModel by viewModel()
    private var snackbar: Snackbar? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View =
            inflater.inflate(R.layout.play_controller, container, false)
                    .apply {
                        setOnClickListener(this@PlayController)
                    }


    override fun onAttach(view: View) {
        super.onAttach(view)
        subscribeIo(viewModel.getStateObservable(), Consumer { setState(it) })
    }

    override fun onDetach(view: View) {
        super.onDetach(view)
        snackbar?.dismiss()
    }

    private fun setState(state: FilterViewModel.State) {
        when (state) {
            is FilterViewModel.State.Loading -> showProgress(state.show)
            is FilterViewModel.State.Error -> displayError(state)
            is FilterViewModel.State.QuizLoaded -> setQuiz(state.quiz)
        }
    }

    private fun displayError(error: FilterViewModel.State.Error) {
        snackbar?.dismiss()
        snackbar = Snackbar.make(view!!, error.message, Snackbar.LENGTH_INDEFINITE)
                .setAction(error.actionText) {
                    error.action
                }
        snackbar?.show()
    }

    private fun showProgress(show: Boolean) {
        view?.apply {
            isEnabled = !show
            playLoading.isVisible = show
        }

    }

    private fun setQuiz(quiz: Quiz) {
        view?.apply {
            if (quiz.isQuizDone()) {
                Snackbar.make(this, R.string.filter_no_more, Snackbar.LENGTH_SHORT).show()
            } else {
                startActivity(QuizActivity.getLaunchIntent(context, quiz))
            }
        }

    }

    override fun onClick(v: View?) {
        viewModel.getQuiz()
    }

    companion object {

        fun newInstance() = PlayController(Bundle.EMPTY)
    }
}