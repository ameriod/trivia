package me.ameriod.trivia.ui.history

import android.content.Context
import me.ameriod.lib.mvp.Mvp
import me.ameriod.lib.mvp.presenter.rx2.BasePresenterRx2
import me.ameriod.lib.mvp.presenter.rx2.IObservableSchedulerRx2
import me.ameriod.trivia.TriviaApplication
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

    companion object {
        fun newInstance(context: Context) = HistoryPresenter(HistoryInteractor((context as TriviaApplication).repository),
                IObservableSchedulerRx2.SUBSCRIBE_IO_OBSERVE_ANDROID_MAIN,
                object : Mvp.ErrorHandler {
                    override fun onError(e: Throwable): String {
                        Timber.e(e, "Error with history")
                        return ""
                    }
                })
    }
}