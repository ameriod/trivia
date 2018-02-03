package me.ameriod.trivia.ui.quiz

import me.ameriod.lib.mvp.Mvp
import me.ameriod.trivia.api.response.Question
import me.ameriod.trivia.ui.quiz.question.Answer

class QuizContract {

    interface View : Mvp.View {
        fun setCurrentQuestion(question: Question, isLastQuestion: Boolean)

        fun setProgress(currentPosition: Int, total: Int)

        fun setCompletedQuiz(quiz: Quiz)
    }

    interface Presenter : Mvp.Presenter<View> {
        fun getNextQuestion(answer: Answer)

        fun getInitialQuestion()
    }

}