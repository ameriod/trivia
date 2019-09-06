package me.ameriod.trivia.ui.history

import android.os.Parcelable
import androidx.lifecycle.ViewModel
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.BehaviorSubject
import kotlinx.android.parcel.Parcelize
import me.ameriod.trivia.api.OpenTriviaRepository
import me.ameriod.trivia.api.db.Result
import timber.log.Timber

class HistoryViewModel(
        private val repository: OpenTriviaRepository
) : ViewModel() {

    private val compositeDisposable: CompositeDisposable = CompositeDisposable()
    val inputSubject: BehaviorSubject<Any> = BehaviorSubject.create()

    init {
        compositeDisposable.add(inputSubject
                .subscribe({
                    Timber.d("input: $it")
                }, {
                    Timber.e(it, "Error handing input")
                }))
    }


    fun getHistory(): Observable<State> = repository.getHistory()
            .map { results ->
                if (results.isEmpty()) State.Empty("No history") else State.Loaded(results)
            }
            .distinctUntilChanged()
            .doOnNext { inputSubject.onNext(State.Loading(false)) }
            .doOnSubscribe { inputSubject.onNext(State.Loading(true)) }


    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

    sealed class State : Parcelable {
        @Parcelize
        data class Loading(val show: Boolean) : State()

        @Parcelize
        data class Empty(val message: String) : State()

        @Parcelize
        data class Loaded(val items: List<Result>) : State()

        @Parcelize
        data class Error(val message: String) : State()
    }

}


