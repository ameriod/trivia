package me.ameriod.trivia.ui.filter

import android.content.Context
import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import kotlinx.android.parcel.Parcelize
import me.ameriod.lib.mvp.presenter.rx2.IObservableSchedulerRx2
import me.ameriod.trivia.R
import me.ameriod.trivia.api.OpenTriviaRepository
import me.ameriod.trivia.api.response.OtCategory
import me.ameriod.trivia.api.response.OtDifficulty
import me.ameriod.trivia.mvvm.BaseViewModel
import me.ameriod.trivia.mvvm.BaseViewState
import me.ameriod.trivia.ui.quiz.Quiz
import timber.log.Timber

class FilterViewModel(
        private val context: Context,
        private val repository: OpenTriviaRepository,
        scheduler: IObservableSchedulerRx2
) : BaseViewModel<FilterViewModel.State>(scheduler) {

    private var filter: Filter = Filter.createDefault(context)
    private var difficulties: List<OtDifficulty> = emptyList()
    private var categories: List<OtCategory> = emptyList()

    fun getFilters() {
        if (difficulties.isEmpty() || categories.isEmpty()) {
            stateSubject.onNext(State.Loading(true))
            addToDisposable(Observable.zip(repository.getDifficulties(), repository.getCategories(),
                    BiFunction { difficulty: List<OtDifficulty>, category: List<OtCategory> ->
                        State.Loaded(
                                difficulties = difficulty,
                                categories = category,
                                selectedFilter = filter
                        )
                    })
                    .compose(scheduler.schedule())
                    .subscribe({
                        difficulties = it.difficulties
                        categories = it.categories
                        stateSubject.onNext(it)
                        stateSubject.onNext(State.Loading(false))
                    }, {
                        Timber.e(it, "Error loading the filters")
                        stateSubject.onNext(State.Error(
                                message = context.getString(R.string.filter_api_error),
                                actionText = context.getString(R.string.filter_retry),
                                action = getFilters()
                        ))
                    }))
        } else {
            stateSubject.onNext(State.Loaded(
                    difficulties = difficulties,
                    categories = categories,
                    selectedFilter = filter
            ))
        }

    }

    fun getQuiz() {
        repository.getQuiz(filter)
        stateSubject.onNext(State.Loading(true))
        addToDisposable(repository.getQuiz(filter)
                .map {
                    State.QuizLoaded(it)
                }
                .compose(scheduler.schedule())
                .subscribe({
                    stateSubject.onNext(State.Loading(false))
                    stateSubject.onNext(it)
                }, {
                    stateSubject.onNext(State.Loading(false))
                    Timber.e(it, "Error loading the quiz")
                    stateSubject.onNext(State.Error(
                            message = context.getString(R.string.filter_api_error),
                            actionText = context.getString(R.string.filter_retry),
                            action = getQuiz()
                    ))
                }))

    }

    fun setQuestionCount(count: Int) {
        filter = filter.copy(count = count)
    }

    fun setDifficulty(difficulty: OtDifficulty) {
        filter = filter.copy(difficulty = difficulty)
    }

    fun setCategories(category: OtCategory) {
        categories.forEach { it.selected = it == category }
        filter = filter.copy(category = category)
        getFilters()
    }

    fun resetFilter() {
        filter = Filter.createDefault(context)
        getFilters()
    }

    sealed class State : BaseViewState {

        @Parcelize
        data class Loading(
                val show: Boolean
        ) : State()

        @Parcelize
        data class Loaded(
                val difficulties: List<OtDifficulty>,
                val categories: List<OtCategory>,
                val selectedFilter: Filter
        ) : State()

        @Parcelize
        data class Error(
                val message: String,
                val actionText: String,
                val action: (Unit)
        ) : State()

        @Parcelize
        data class QuizLoaded(
                val quiz: Quiz
        ) : State()

    }
}