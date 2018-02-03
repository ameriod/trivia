package me.ameriod.trivia.ui.filter

import android.content.Context
import android.os.Bundle
import me.ameriod.lib.mvp.Mvp
import me.ameriod.lib.mvp.presenter.rx2.BasePresenterRx2
import me.ameriod.lib.mvp.presenter.rx2.IObservableSchedulerRx2
import me.ameriod.trivia.R
import me.ameriod.trivia.api.response.Category
import me.ameriod.trivia.api.response.Difficulty
import timber.log.Timber

class FilterPresenter(private val defaultFilter: QuizFilter,
                      private val interactor: FilterContract.Interactor,
                      schedulerRx2: IObservableSchedulerRx2,
                      errorHandler: Mvp.ErrorHandler) :
        BasePresenterRx2<FilterContract.View>(schedulerRx2, errorHandler), FilterContract.Presenter {

    private var quizFilter: QuizFilter = defaultFilter
    private var categories: List<Category> = emptyList()
    private var difficulties: List<Difficulty> = emptyList()

    override fun saveState(outState: Bundle) {
        super.saveState(outState)
        outState.putParcelable(OUT_FILTER, quizFilter)
    }

    override fun restoreState(savedState: Bundle) {
        super.restoreState(savedState)
        quizFilter = savedState.getParcelable(OUT_FILTER)
    }

    override fun getFilter() {
        if (difficulties.isEmpty() || categories.isEmpty()) {
            getView().showProgress(true)
            addDisposable(interactor.getDifficulties()
                    .flatMap { difficulties ->
                        interactor.getCategories()
                                .map { categories ->
                                    categories to difficulties
                                }
                    }
                    .compose(scheduler.schedule())
                    .subscribe({ pair ->
                        this.categories = pair.first
                        getView().setCategories(pair.first, quizFilter.category)
                        this.difficulties = pair.second
                        getView().setDifficulties(pair.second, quizFilter.difficulty)
                        getView().setQuestionCount(quizFilter.count.toString())
                        getView().showProgress(false)
                    }, { throwable ->
                        getView().displayError(errorHandler.onError(throwable))
                    }))
        } else {
            getView().setCategories(categories, quizFilter.category)
            getView().setDifficulties(difficulties, quizFilter.difficulty)
            getView().setQuestionCount(quizFilter.count.toString())
        }

    }

    override fun getQuestions() {
        getView().showProgress(true)
        addDisposable(interactor.getQuestions(quizFilter)
                .compose(scheduler.schedule())
                .subscribe({ quiz ->
                    getView().showProgress(false)
                    getView().setQuiz(quiz)
                }, { throwable ->
                    getView().displayError(errorHandler.onError(throwable))
                }))
    }


    override fun setDifficulty(difficulty: Difficulty) {
        if (quizFilter.difficulty == difficulty) {
            return
        }
        quizFilter.difficulty = difficulty
    }

    override fun setCount(count: Int) {
        if (quizFilter.count == count) {
            return
        }
        quizFilter.count = count
    }

    override fun setCategory(category: Category) {
        if (quizFilter.category == category) {
            return
        }
        // check the right category
        categories.map { item ->
            item.selected = item.id == category.id
        }

        quizFilter.category = category

        // update the ui with the selection
        getView().setCategories(categories, quizFilter.category)
    }

    override fun resetFilter() {
        // set defaults
        quizFilter = defaultFilter
        difficulties = emptyList()
        categories = emptyList()
        // re-set
        getFilter()
    }

    companion object {

        private const val OUT_FILTER = "out_filter"

        fun newInstance(context: Context) = FilterPresenter(QuizFilter.createDefault(context),
                FilterInteractor(context), IObservableSchedulerRx2.SUBSCRIBE_IO_OBSERVE_ANDROID_MAIN,
                object : Mvp.ErrorHandler {
                    override fun onError(e: Throwable): String {
                        Timber.e(e, "Error loading questions for the filter params")
                        return context.getString(R.string.filter_api_error)
                    }
                })
    }
}