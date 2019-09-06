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
    // input is how the view can interact with the view model
    val inputSubject: BehaviorSubject<Any> = BehaviorSubject.create()
    // state can only be changed in the view model
    private val stateSubject: BehaviorSubject<State> = BehaviorSubject.create()

    init {
        compositeDisposable.add(inputSubject
                .subscribe({
                    Timber.d("input: $it")
                }, {
                    Timber.e(it, "Error handing input")
                }))
    }


    fun getHistory(): Observable<State> = stateSubject
            .doOnSubscribe {
                compositeDisposable.add(repository.getHistory()
                        .map { results ->
                            if (results.isEmpty()) State.Empty("No history") else State.Loaded(results)
                        }
                        .distinctUntilChanged()
                        .doOnNext { stateSubject.onNext(State.Loading(false)) }
                        .doOnSubscribe { stateSubject.onNext(State.Loading(true)) }
                        .subscribe {
                            stateSubject.onNext(it)
                        })
            }

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


