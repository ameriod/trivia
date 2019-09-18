package me.ameriod.trivia.mvvm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

abstract class BaseViewModel<S : BaseViewState>(
        protected val scheduler: IObservableSchedulerRx2
) : ViewModel() {

    private val compositeDisposable: CompositeDisposable = CompositeDisposable()
    val stateLiveData: MutableLiveData<S> = MutableLiveData()

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