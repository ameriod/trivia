package me.ameriod.trivia.ui.history

import androidx.lifecycle.ViewModel
import io.reactivex.Observable
import me.ameriod.trivia.api.OpenTriviaRepository
import me.ameriod.trivia.api.db.Result

class HistoryViewModel(
        private val repository: OpenTriviaRepository
) : ViewModel() {

    fun getHistory() : Observable<List<Result>> = repository.getHistory()

}