package me.ameriod.trivia.ui.result

import io.reactivex.Observable
import me.ameriod.trivia.api.OpenTriviaRepository
import me.ameriod.trivia.api.db.Result

class ResultInteractor(private val repository: OpenTriviaRepository) : ResultContract.Interactor {

    override fun getResult(resultId: Long): Observable<Result> = repository.getResult(resultId)

}