package me.ameriod.trivia.ui.result

import android.content.Context
import me.ameriod.lib.mvp.Mvp
import me.ameriod.lib.mvp.presenter.rx2.BasePresenterRx2
import me.ameriod.lib.mvp.presenter.rx2.IObservableSchedulerRx2
import me.ameriod.trivia.TriviaApplication
import me.ameriod.trivia.ui.adapter.TriviaAdapterItem
import me.ameriod.trivia.ui.result.recycler.ResultGraphItem
import timber.log.Timber

class ResultPresenter(private val interactor: ResultContract.Interactor,
                      schedulerRx2: IObservableSchedulerRx2,
                      errorHandler: Mvp.ErrorHandler) : BasePresenterRx2<ResultContract.View>(schedulerRx2, errorHandler), ResultContract.Presenter {
    override fun getResult(resultId: Long) {
        addDisposable(interactor.getResult(resultId)
                .map { result ->
                    val items = mutableListOf<TriviaAdapterItem>()
                    items.add(ResultGraphItem(result.totalQuestions, result.correctQuestions, result.incorrectQuestions))
                    items.addAll(result.getItems())
                    items
                }
                .compose(scheduler.schedule())
                .subscribe({ items ->
                    getView().setResult(items)
                }, { throwable ->
                    Timber.e(throwable, "Error getting a result")
                }))
    }

}