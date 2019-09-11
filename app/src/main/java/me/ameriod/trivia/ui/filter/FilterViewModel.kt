package me.ameriod.trivia.ui.filter

import android.content.Context
import io.reactivex.Observable
import io.reactivex.functions.Action
import io.reactivex.functions.BiFunction
import io.reactivex.functions.Consumer
import io.reactivex.functions.Function
import io.reactivex.subjects.BehaviorSubject
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
import android.icu.lang.UCharacter.GraphemeClusterBreak.T


class FilterViewModel(
        private val context: Context,
        private val repository: OpenTriviaRepository,
        scheduler: IObservableSchedulerRx2
) : BaseViewModel<FilterViewModel.State>(scheduler) {

    private var filter: Filter = Filter.createDefault(context)
    private var difficulties: List<OtDifficulty> = emptyList()
    private var categories: List<OtCategory> = emptyList()

    private val questionCountSubject: BehaviorSubject<Int> = BehaviorSubject.create()
    private val categorySubject: BehaviorSubject<OtCategory> = BehaviorSubject.create()
    private val difficultySubject: BehaviorSubject<OtDifficulty> = BehaviorSubject.create()

    init {
        addToDisposable(questionCountSubject
                .map { value ->
                    // Can only be 1 to 50
                    var input = value
                    if (input <= 0) {
                        input = 1
                    } else if (input > 50) {
                        input = 50
                    }
                    input
                }
                .distinctUntilChanged()
                .compose(scheduler.schedule())
                .subscribe({
                    filter = filter.copy(count = it)
                    getFilters()
                }, {
                    Timber.e(it, "Error observing question count")
                }))

        addToDisposable(categorySubject
                .distinctUntilChanged()
                .subscribe({ newCategory ->
                    categories.forEach { it.selected = it == newCategory }
                    filter = filter.copy(category = newCategory)
                    getFilters()
                }, {
                    Timber.e(it, "Error observing selected category")
                }))

        addToDisposable(difficultySubject
                .distinctUntilChanged()
                .subscribe({
                    filter = filter.copy(difficulty = it)
                    getFilters()
                }, {
                    Timber.e(it, "Error observing selected category")
                }))
    }

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
                .compose(scheduler.schedule())
                .subscribe({
                    stateSubject.onNext(State.Loading(false))
                    stateSubject.onNext(State.QuizLoaded(it))
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

    /**
     * Can also be a RxJava [Consumer]
     */
    fun takeQuestionCount(): (Int) -> Unit = { questionCountSubject.onNext(it) }

    /**
     * Can also be a RxJava [Consumer]
     */
    fun takeDifficulty(): (OtDifficulty) -> Unit = { difficultySubject.onNext(it) }

    fun setCategories(category: OtCategory) {
        categorySubject.onNext(category)
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