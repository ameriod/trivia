package me.ameriod.trivia.ui.filter

import android.content.Context
import io.reactivex.Observable
import me.ameriod.trivia.TriviaApplication
import me.ameriod.trivia.api.OpenTriviaRepository
import me.ameriod.trivia.api.response.OtCategory
import me.ameriod.trivia.api.response.OtDifficulty
import me.ameriod.trivia.ui.quiz.Quiz

class FilterInteractor(private val repository: OpenTriviaRepository) : FilterContract.Interactor {

    constructor(context: Context) :
            this((context.applicationContext as TriviaApplication).repository)

    override fun getQuiz(filter: Filter): Observable<Quiz> =
            repository.getQuiz(filter)

    override fun getDifficulties(): Observable<List<OtDifficulty>> =
            repository.getDifficulties()

    override fun getCategories(): Observable<List<OtCategory>> =
            repository.getCategories()
}
