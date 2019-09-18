package me.ameriod.trivia.ui.result

import kotlinx.android.parcel.Parcelize
import me.ameriod.trivia.api.OpenTriviaRepository
import me.ameriod.trivia.mvvm.BaseViewModel
import me.ameriod.trivia.mvvm.BaseViewState
import me.ameriod.trivia.mvvm.IObservableSchedulerRx2
import me.ameriod.trivia.ui.adapter.TriviaAdapterItem
import me.ameriod.trivia.ui.result.recycler.ResultGraphItem
import timber.log.Timber

class ResultViewModel(
        private val repository: OpenTriviaRepository,
        schedulerRx2: IObservableSchedulerRx2
) : BaseViewModel<ResultViewModel.State>(schedulerRx2) {

    fun getResult(resultId: Long) {
        stateSubject.onNext(State.Loading(true))
        addToDisposable(repository.getResult(resultId)
                .compose(scheduler.schedule())
                .subscribe({
                    val items = mutableListOf<TriviaAdapterItem>()
                            .apply {
                                add(ResultGraphItem(it.totalQuestions, it.correctQuestions, it.incorrectQuestions))
                                addAll(it.getItems())
                            }
                    stateSubject.onNext(State.Result(items))
                    stateSubject.onNext(State.Loading(false))
                }, {
                    Timber.e(it, "Error loading results")
                }))
    }

    sealed class State : BaseViewState {

        @Parcelize
        data class Loading(
                val show: Boolean
        ) : State()

        @Parcelize
        data class Result(
                val items: List<TriviaAdapterItem>
        ) : State()

    }
}
