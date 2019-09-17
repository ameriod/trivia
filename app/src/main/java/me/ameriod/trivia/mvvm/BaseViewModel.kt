package me.ameriod.trivia.mvvm

import androidx.lifecycle.ViewModel
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import me.ameriod.lib.mvp.presenter.rx2.IObservableSchedulerRx2

abstract class BaseViewModel<S : BaseViewState>(
        protected val scheduler: IObservableSchedulerRx2
) : ViewModel() {

    private val compositeDisposable: CompositeDisposable = CompositeDisposable()
    protected val stateSubject: PublishSubject<S> = PublishSubject.create()

    fun getStateObservable(): Observable<S> = stateSubject
            .distinctUntilChanged()

    protected fun addToDisposable(disposable: Disposable) {
        compositeDisposable.add(disposable)
    }

    protected fun addToDisposable(vararg disposables: Disposable) {
        compositeDisposable.addAll(*disposables)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}