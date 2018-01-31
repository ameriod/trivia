package me.ameriod.trivia.ui.filter

import android.content.Context
import android.os.Bundle
import me.ameriod.lib.mvp.Mvp
import me.ameriod.lib.mvp.R
import me.ameriod.lib.mvp.presenter.rx2.BasePresenterRx2
import me.ameriod.lib.mvp.presenter.rx2.IObservableSchedulerRx2
import me.ameriod.trivia.api.response.Category
import me.ameriod.trivia.api.response.Difficulty
import timber.log.Timber

class FilterPresenter(private val defaultFilter: QuizFilter,
                      private val interactor: FilterContract.Interactor,
                      schedulerRx2: IObservableSchedulerRx2,
                      errorHandler: Mvp.ErrorHandler) :
        BasePresenterRx2<FilterContract.View>(schedulerRx2, errorHandler), FilterContract.Presenter {

    private var quizFilter: QuizFilter = defaultFilter

    override fun saveState(outState: Bundle) {
        super.saveState(outState)
        outState.putParcelable(OUT_FILTER, quizFilter)
    }

    override fun restoreState(savedState: Bundle) {
        super.restoreState(savedState)
        quizFilter = savedState.getParcelable(OUT_FILTER)
    }

    override fun getFilter() {
        getView().setFilter(quizFilter)
    }

    override fun getQuestions() {
        addDisposable(interactor.getQuestions(quizFilter)
                .compose(scheduler.schedule())
                .subscribe({ questions ->
                    getView().showProgress(false)
                    getView().setQuestions(questions)
                }, { throwable ->
                    getView().displayError(errorHandler.onError(throwable))
                    getView().showProgress(false)
                }))
    }


    override fun setDifficulty(difficulty: Difficulty) {
        if (quizFilter.difficulty == difficulty) {
            return
        }
        quizFilter = QuizFilter(quizFilter.count, difficulty, quizFilter.category)
        getFilter()
    }

    override fun setCount(count: Int) {
        if (quizFilter.count == count) {
            return
        }
        quizFilter = QuizFilter(count, quizFilter.difficulty, quizFilter.category)
        getFilter()
    }

    override fun setCategory(category: Category) {
        if (quizFilter.category == category) {
            return
        }
        quizFilter = QuizFilter(quizFilter.count, quizFilter.difficulty, category)
        getFilter()
    }

    override fun resetFilter() {
        quizFilter = defaultFilter
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