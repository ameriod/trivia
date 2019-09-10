package me.ameriod.trivia.mvvm

import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.BehaviorSubject
import me.ameriod.lib.mvp.presenter.rx2.IObservableSchedulerRx2

abstract class BaseViewModel<S : BaseViewState>(
        protected val scheduler: IObservableSchedulerRx2
) : ViewModel() {

    private val compositeDisposable: CompositeDisposable = CompositeDisposable()
    val stateSubject: BehaviorSubject<S> = BehaviorSubject.create()

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