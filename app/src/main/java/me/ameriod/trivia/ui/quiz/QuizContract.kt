package me.ameriod.trivia.ui.quiz

import io.reactivex.Observable
import me.ameriod.lib.mvp.Mvp
import me.ameriod.trivia.ui.quiz.question.Answer
import me.ameriod.trivia.ui.quiz.question.Question

class QuizContract {

    interface View : Mvp.View {
        fun setCurrentQuestion(question: Question, isLastQuestion: Boolean)

        fun setProgress(currentPosition: Int, total: Int)

        fun setCompletedQuiz(resultId: Long)

        fun onTimeUpdated(formattedTime: String)
    }

    interface Presenter : Mvp.Presenter<View> {
        fun getNextQuestion(answer: Answer)

        fun getInitialQuestion()

        fun startQuizTimer()
    }

    interface Interactor {
        fun saveResults(quiz: Quiz): Observable<Long>
    }

}