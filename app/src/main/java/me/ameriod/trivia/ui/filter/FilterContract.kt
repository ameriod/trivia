package me.ameriod.trivia.ui.filter

import io.reactivex.Observable
import me.ameriod.lib.mvp.Mvp
import me.ameriod.trivia.api.response.Category
import me.ameriod.trivia.api.response.Difficulty
import me.ameriod.trivia.api.response.Question

class FilterContract {

    interface View : Mvp.View {

        fun setQuestions(items: List<Question>)

        fun setFilter(filter: QuizFilter)

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
        fun getQuestions(filter: QuizFilter): Observable<List<Question>>
    }
}