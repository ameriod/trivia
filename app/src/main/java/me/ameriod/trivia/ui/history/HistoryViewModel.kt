package me.ameriod.trivia.ui.history

import android.content.Context
import kotlinx.android.parcel.Parcelize
import me.ameriod.trivia.R
import me.ameriod.trivia.api.OpenTriviaRepository
import me.ameriod.trivia.api.db.Result
import me.ameriod.trivia.mvvm.BaseViewModel
import me.ameriod.trivia.mvvm.BaseViewState
import timber.log.Timber

class HistoryViewModel(
        private val context: Context,
        private val repository: OpenTriviaRepository
) : BaseViewModel<HistoryViewModel.State>() {

    fun getHistory() {
        stateSubject.onNext(State.Loading(true))
        addToDisposable(repository.getHistory()
                .map { results ->
                    if (results.isEmpty())
                        State.Empty(context.getString(R.string.history_empty))
                    else
                        State.Loaded(results)
                }
                .distinctUntilChanged()
                .doOnNext { }
                .subscribe({
                    stateSubject.onNext(it)
                    stateSubject.onNext(State.Loading(false))
                }) {
                    Timber.e(it, "Error loading history")
                    stateSubject.onNext(State.Error(
                            message = context.getString(R.string.history_error),
                            actionText = context.getString(R.string.history_error_action)
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
                val message: String
        ) : State()

        @Parcelize
        data class Loaded(
                val items: List<Result>
        ) : State()

        @Parcelize
        data class Error(
                val message: String,
                val actionText: String
        ) : State()

    }

}


