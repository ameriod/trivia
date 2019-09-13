package me.ameriod.trivia.ui.quiz

import io.reactivex.Observable
import me.ameriod.trivia.api.OpenTriviaRepository
import me.ameriod.trivia.mvvm.BaseViewModel
import me.ameriod.trivia.mvvm.BaseViewState
import me.ameriod.trivia.ui.quiz.question.Answer
import timber.log.Timber
import java.util.concurrent.TimeUnit

class QuizViewModel(
        private val repository: OpenTriviaRepository,
        private val quiz: Quiz
) : BaseViewModel<QuizViewModel.State>() {

     fun getInitialQuestion() {
        getQuestion(true)
    }

     fun getNextQuestion(answer: Answer) {
      //  quiz.setAnswer(answer)
        getQuestion(false)
    }

    private fun getQuestion(isInitial: Boolean) {
      //  val view = getView()
        if (quiz.isQuizDone()) {
            addToDisposable(repository.saveQuizAsResult(quiz)
                    .compose(scheduler.schedule())
                    .subscribe({ resultId ->
                      //  getView().setCompletedQuiz(resultId)
                    }, { throwable ->
                        Timber.e(throwable, "Error with saving quiz results")
                    }))
        } else {
            val question = if (isInitial) quiz.getCurrentQuestion() else quiz.getNextQuestion()
//            view.setProgress(quiz.getCurrentPosition(), quiz.getNumberOfQuestions())
//            view.setCurrentQuestion(question, quiz.isLastQuestion())
        }
    }

     fun startQuizTimer() {
       // getView().onTimeUpdated(getFormattedTime())
        addToDisposable(Observable.interval(1, TimeUnit.SECONDS)
                .compose(scheduler.schedule())
                .map { _ ->
                    // ticking every second, but use the quiz start time, already saved in the bundle
                    getFormattedTime()
                }
                .subscribe({ formatted ->
             //       getView().onTimeUpdated(formatted)
                }, { throwable ->
                    Timber.e(throwable, "Error with quiz timer")
                }))
    }

    fun getFormattedTime(): String {
        val totalTime = (System.currentTimeMillis() - quiz.startTime) / 1000
        return String.format("%02d:%02d", totalTime % 3600 / 60, totalTime % 60)
    }


    sealed class State : BaseViewState {


    }
}