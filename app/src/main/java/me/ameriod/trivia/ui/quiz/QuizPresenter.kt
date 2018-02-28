package me.ameriod.trivia.ui.quiz

import android.os.Bundle
import io.reactivex.Observable
import me.ameriod.lib.mvp.Mvp
import me.ameriod.lib.mvp.presenter.rx2.BasePresenterRx2
import me.ameriod.lib.mvp.presenter.rx2.IObservableSchedulerRx2
import me.ameriod.trivia.ui.quiz.question.Answer
import timber.log.Timber
import java.util.concurrent.TimeUnit


class QuizPresenter(private var quiz: Quiz,
                    schedulerRx2: IObservableSchedulerRx2,
                    errorHandler: Mvp.ErrorHandler) :
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

    override fun getNextQuestion(answer: Answer) {
        quiz.setAnswer(answer)
        getQuestion(false)
    }

    private fun getQuestion(isInitial: Boolean) {
        val view = getView()
        if (quiz.isQuizDone()) {
            view.setCompletedQuiz(quiz.toResult())
        } else {
            val question = if (isInitial) quiz.getCurrentQuestion() else quiz.getNextQuestion()
            view.setProgress(quiz.getCurrentPosition(), quiz.getNumberOfQuestions())
            view.setCurrentQuestion(question, quiz.isLastQuestion())
        }
    }

    override fun startQuizTimer() {
        getView().onTimeUpdated(getFormattedTime())
        addDisposable(Observable.interval(1, TimeUnit.SECONDS)
                .compose(scheduler.schedule())
                .map { _ ->
                    // ticking every second, but use the quiz start time, already saved in the bundle
                    getFormattedTime()
                }
                .subscribe({ formatted ->
                    getView().onTimeUpdated(formatted)
                }, { throwable ->
                    Timber.e(throwable, "Error with quiz timer")
                }))
    }

    fun getFormattedTime(): String {
        val totalTime = (System.currentTimeMillis() - quiz.startTime) / 1000
        return String.format("%02d:%02d", totalTime % 3600 / 60, totalTime % 60)
    }

    companion object {
        private const val OUT_STATE = "out_quiz"

        fun newInstance(quiz: Quiz) = QuizPresenter(quiz, IObservableSchedulerRx2.SUBSCRIBE_IO_OBSERVE_ANDROID_MAIN,
                object : Mvp.ErrorHandler {
                    override fun onError(e: Throwable): String {
                        Timber.e(e, "Error with quiz")
                        return ""
                    }
                })
    }
}