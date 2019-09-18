package me.ameriod.trivia.ui.history

import android.content.Context
import kotlinx.android.parcel.Parcelize
import me.ameriod.trivia.R
import me.ameriod.trivia.api.OpenTriviaRepository
import me.ameriod.trivia.api.db.Result
import me.ameriod.trivia.mvvm.BaseViewModel
import me.ameriod.trivia.mvvm.BaseViewState
import me.ameriod.trivia.mvvm.IObservableSchedulerRx2
import timber.log.Timber

class HistoryViewModel(
        private val context: Context,
        private val repository: OpenTriviaRepository,
        scheduler: IObservableSchedulerRx2
) : BaseViewModel<HistoryViewModel.State>(scheduler) {

    fun getHistory() {
        stateSubject.onNext(State.Empty(false))
        stateSubject.onNext(State.Loading(true))
        addToDisposable(repository.getHistory()
                .map { results ->
                    if (results.isEmpty())
                        State.Empty(true)
                    else
                        State.Loaded(results)
                }
                .distinctUntilChanged()
                .compose(scheduler.schedule())
                .subscribe({
                    stateSubject.onNext(it)
                    stateSubject.onNext(State.Loading(false))
                }) {
                    Timber.e(it, "Error loading history")
                    stateSubject.onNext(State.Error(
                            message = context.getString(R.string.history_error),
                            actionText = context.getString(R.string.history_error_action),
                            action = getHistory()
                    ))
                    stateSubject.onNext(State.Loading(false))
                })
    }

    sealed class State : BaseViewState {

        @Parcelize
        data class Loading(
                val show: Boolean
        ) : State()

        @Parcelize
        data class Empty(
                val show: Boolean
        ) : State()

        @Parcelize
        data class Loaded(
                val items: List<Result>
        ) : State()

        @Parcelize
        data class Error(
                val message: String,
                val actionText: String,
                val action: Unit
        ) : State()

    }

}
