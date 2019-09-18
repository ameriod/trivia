package me.ameriod.trivia.ui.quiz

import io.reactivex.Observable
import kotlinx.android.parcel.Parcelize
import me.ameriod.trivia.api.OpenTriviaRepository
import me.ameriod.trivia.mvvm.BaseViewModel
import me.ameriod.trivia.mvvm.BaseViewState
import me.ameriod.trivia.mvvm.IObservableSchedulerRx2
import me.ameriod.trivia.ui.quiz.question.Answer
import me.ameriod.trivia.ui.quiz.question.Question
import timber.log.Timber
import java.util.concurrent.TimeUnit

class QuizViewModel(
        private val quiz: Quiz,
        private val repository: OpenTriviaRepository,
        schedulerRx2: IObservableSchedulerRx2
) : BaseViewModel<QuizViewModel.State>(schedulerRx2) {

    fun getInitialQuestion() {
        getQuestion(true)
    }

    fun onQuestionAnswered(answer: Answer) {
        quiz.setAnswer(answer)
        getQuestion(false)
    }

    private fun getQuestion(isInitial: Boolean) {
        if (quiz.isQuizDone()) {
            addToDisposable(repository.saveQuizAsResult(quiz)
                    .compose(scheduler.schedule())
                    .subscribe({ resultId ->
                        stateLiveData.value = State.OnQuizFinished(resultId)
                    }, { throwable ->
                        Timber.e(throwable, "Error with saving quiz results")
                    }))
        } else {
            val question = if (isInitial) quiz.getCurrentQuestion() else quiz.getNextQuestion()
            stateLiveData.value = State.DisplayQuestion(
                    currentPosition = quiz.getCurrentPosition(),
                    totalQuestions = quiz.getNumberOfQuestions(),
                    isLastQuestion = quiz.isLastQuestion(),
                    question = question
            )
        }
    }

    fun startQuizTimer() {
        stateLiveData.value = getFormattedTime()
        addToDisposable(Observable.interval(1, TimeUnit.SECONDS)
                .compose(scheduler.schedule())
                .map { _ ->
                    // ticking every second, but use the quiz start time
                    getFormattedTime()
                }
                .subscribe({ formatted ->
                    stateLiveData.value = formatted
                }, { throwable ->
                    Timber.e(throwable, "Error with quiz timer")
                }))
    }

    private fun getFormattedTime(): State.OnTimeUpdated {
        val totalTime = (System.currentTimeMillis() - quiz.startTime) / 1000
        return State.OnTimeUpdated(String.format("%02d:%02d", totalTime % 3600 / 60, totalTime % 60))
    }

    sealed class State : BaseViewState {

        @Parcelize
        data class DisplayQuestion(
                val currentPosition: Int,
                val totalQuestions: Int,
                val isLastQuestion: Boolean,
                val question: Question
        ) : State()

        @Parcelize
        data class OnQuizFinished(
                val resultId: Long
        ) : State()

        @Parcelize
        data class OnTimeUpdated(
                val formattedTime: String
        ) : State()
    }
}