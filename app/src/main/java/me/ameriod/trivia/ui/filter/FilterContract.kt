package me.ameriod.trivia.ui.filter

import io.reactivex.Observable
import me.ameriod.lib.mvp.Mvp
import me.ameriod.trivia.api.response.OtCategory
import me.ameriod.trivia.api.response.OtDifficulty
import me.ameriod.trivia.ui.quiz.Quiz

class FilterContract {

    interface View : Mvp.View {

        fun setQuiz(quiz: Quiz)

        fun setQuestionCount(count: String)

        fun setCategories(categories: List<OtCategory>, selectedItem: OtCategory)

        fun setDifficulties(difficulties: List<OtDifficulty>, selectedItem: OtDifficulty)

    }

    interface Presenter : Mvp.Presenter<View> {

        fun setDifficulty(difficulty: OtDifficulty)

        fun setCount(count: Int)

        fun setCategory(category: OtCategory)

        fun getFilter()

        fun getQuestions()

        fun resetFilter()
    }

    interface Interactor {

        fun getQuiz(filter: Filter): Observable<Quiz>

        fun getDifficulties(): Observable<List<OtDifficulty>>

        fun getCategories(): Observable<List<OtCategory>>

    }
}