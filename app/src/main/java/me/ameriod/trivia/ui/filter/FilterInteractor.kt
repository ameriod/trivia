package me.ameriod.trivia.ui.filter

import android.content.Context
import io.reactivex.Observable
import me.ameriod.trivia.TriviaApplication
import me.ameriod.trivia.api.TriviaRepository
import me.ameriod.trivia.api.response.Category
import me.ameriod.trivia.api.response.Difficulty
import me.ameriod.trivia.api.response.Question

class FilterInteractor(private val repository: TriviaRepository) : FilterContract.Interactor {

    constructor(context: Context) :
            this((context.applicationContext as TriviaApplication).repository)

    override fun getQuestions(filter: QuizFilter): Observable<List<Question>> =
            repository.getQuestions(filter)
                    .map { response ->
                        response.results
                    }

    override fun getDifficulties(): Observable<List<Difficulty>> =
            repository.getDifficulties()

    override fun getCategories(): Observable<List<Category>> =
            repository.getCategories()
}
