package me.ameriod.trivia.ui.history

import me.ameriod.lib.mvp.Mvp
import me.ameriod.lib.mvp.presenter.rx2.BasePresenterRx2
import me.ameriod.lib.mvp.presenter.rx2.IObservableSchedulerRx2
import timber.log.Timber

class HistoryPresenter(private val interactor: HistoryContract.Interactor,
                       schedulerRx2: IObservableSchedulerRx2,
                       errorHandler: Mvp.ErrorHandler) : BasePresenterRx2<HistoryContract.View>(schedulerRx2, errorHandler), HistoryContract.Presenter {

    override fun getHistory() {
        addDisposable(interactor.getHistory().compose(scheduler.schedule())
                .subscribe({ history ->
                    getView().setHistory(history)
                }, { throwable ->
                    Timber.e(throwable, "Error loading history...")

                }))
    }

}