package me.ameriod.trivia.ui.history

import io.reactivex.Observable
import me.ameriod.trivia.api.OpenTriviaRepository
import me.ameriod.trivia.api.db.Result

class HistoryInteractor(private val repository: OpenTriviaRepository) : HistoryContract.Interactor {
    override fun getHistory(): Observable<List<Result>> = repository.getHistory()
}