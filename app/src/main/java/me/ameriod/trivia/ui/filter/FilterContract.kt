package me.ameriod.trivia.ui.filter

import io.reactivex.Observable
import me.ameriod.lib.mvp.Mvp
import me.ameriod.trivia.api.response.Category
import me.ameriod.trivia.api.response.Difficulty
import me.ameriod.trivia.api.response.Question
import me.ameriod.trivia.ui.quiz.Quiz

class FilterContract {

    interface View : Mvp.View {

        fun setQuiz(quiz: Quiz)

        fun setQuestionCount(count: String)

        fun setCategories(categories: List<Category>, selectedItem: Category)

        fun setDifficulties(difficulties: List<Difficulty>, selectedItem: Difficulty)

    }

    interface Presenter : Mvp.Presenter<View> {

        fun setDifficulty(difficulty: Difficulty)

        fun setCount(count: Int)

        fun setCategory(category: Category)

        fun getFilter()

        fun getQuestions()

        fun resetFilter()
    }

    interface Interactor {

        fun getQuestions(filter: QuizFilter): Observable<Quiz>

        fun getDifficulties(): Observable<List<Difficulty>>

        fun getCategories(): Observable<List<Category>>

    }
}