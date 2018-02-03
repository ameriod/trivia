package me.ameriod.trivia.ui.quiz

import android.os.Bundle
import me.ameriod.lib.mvp.Mvp
import me.ameriod.lib.mvp.presenter.rx2.BasePresenterRx2
import me.ameriod.lib.mvp.presenter.rx2.IObservableSchedulerRx2
import timber.log.Timber

class QuizPresenter(private var quiz: Quiz,
                    schedulerRx2: IObservableSchedulerRx2 = IObservableSchedulerRx2.SUBSCRIBE_IO_OBSERVE_ANDROID_MAIN,
                    errorHandler: Mvp.ErrorHandler = object : Mvp.ErrorHandler {
                        override fun onError(e: Throwable): String {
                            Timber.e(e, " Error with quiz")
                            return ""
                        }
                    }) :
        BasePresenterRx2<QuizContract.View>(schedulerRx2, errorHandler), QuizContract.Presenter {


    override fun restoreState(savedState: Bundle) {
        super.restoreState(savedState)
        quiz = savedState.getParcelable(OUT_STATE)
    }

    override fun saveState(outState: Bundle) {
        super.saveState(outState)
        outState.putParcelable(OUT_STATE, quiz)
    }

    override fun getInitialQuestion() {
        getQuestion(true)
    }

    override fun getNextQuestion(answer: String) {
        quiz.setAnswer(answer)
        getQuestion(false)
    }

    private fun getQuestion(isInitial: Boolean) {
        val view = getView()
        if (quiz.isQuizDone()) {
            view.setCompletedQuiz(quiz)
        } else {
            val question = if (isInitial) quiz.getCurrentQuestion() else quiz.getNextQuestion()
            view.setProgress(quiz.getCurrentPosition(), quiz.getNumberOfQuestions())
            view.setCurrentQuestion(question, quiz.isLastQuestion())
        }
    }

    companion object {
        private const val OUT_STATE = "out_quiz"
    }
}