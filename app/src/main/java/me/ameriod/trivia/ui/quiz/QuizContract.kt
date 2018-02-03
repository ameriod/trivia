package me.ameriod.trivia.ui.quiz

import me.ameriod.lib.mvp.Mvp
import me.ameriod.trivia.api.response.Question

class QuizContract {

    interface View : Mvp.View {
        fun setCurrentQuestion(question: Question, isLastQuestion: Boolean)

        fun setProgress(currentPosition: Int, total: Int)

        fun setCompletedQuiz(quiz: Quiz)
    }

    interface Presenter : Mvp.Presenter<View> {
        fun getNextQuestion(answer: String)

        fun getInitialQuestion()
    }

}