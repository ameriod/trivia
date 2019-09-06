package me.ameriod.trivia.ui

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import com.bluelinelabs.conductor.Controller
import com.bluelinelabs.conductor.archlifecycle.LifecycleController
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import me.ameriod.lib.mvp.presenter.rx2.IObservableSchedulerRx2
import me.ameriod.trivia.di.RxSchedulerName
import me.ameriod.trivia.di.inject
import org.koin.core.qualifier.named
import timber.log.Timber

/**
 * [LifecycleController] with a [ViewModelStore]
 */
abstract class MvvmController : LifecycleController, ViewModelStoreOwner {

    constructor() : super()

    constructor(args: Bundle?) : super(args)

    private val store by lazy(LazyThreadSafetyMode.NONE) { ViewModelStore() }

    /**
     * [IObservableSchedulerRx2.SUBSCRIBE_IO_OBSERVE_ANDROID_MAIN]
     */
    protected val schedulerIo: IObservableSchedulerRx2 by inject()

    /**
     * [IObservableSchedulerRx2.SUBSCRIBE_COMPUTATION_OBSERVE_ANDROID_MAIN]
     */
    protected val schedulerComp: IObservableSchedulerRx2 by inject(named(RxSchedulerName.SUBSCRIBE_COMPUTATION_OBSERVE_ANDROID_MAIN.name))

    private var compositeDisposable: CompositeDisposable? = null

    override fun onAttach(view: View) {
        super.onAttach(view)
        compositeDisposable = CompositeDisposable()
    }

    override fun onDetach(view: View) {
        super.onDetach(view)
        compositeDisposable?.dispose()
    }

    protected fun <T> subscribeIo(observable: Observable<T>,
                                  onNext: (result: T) -> Unit) {
        subscribeIo(observable = observable,
                onNext = onNext,
                onError = { Timber.e(it, "Error") })
    }

    protected fun <T> subscribeIo(observable: Observable<T>,
                                  onNext: (result: T) -> Unit,
                                  onError: (throwable: Throwable) -> Unit) {
        addToDisposable(observable
                .compose(schedulerIo.schedule())
                .subscribe(onNext, onError))
    }

    protected fun <T> subscribeComp(observable: Observable<T>,
                                    onNext: (result: T) -> Unit,
                                    onError: (throwable: Throwable) -> Unit) {
        addToDisposable(observable
                .compose(schedulerComp.schedule())
                .subscribe(onNext, onError))
    }

    protected fun <T> subscribeComp(observable: Observable<T>,
                                    onNext: (result: T) -> Unit) {
        subscribeComp(observable = observable,
                onNext = onNext,
                onError = { Timber.e(it, "Error") })
    }

    protected fun addToDisposable(disposable: Disposable) {
        compositeDisposable?.add(disposable)
                ?: throw IllegalStateException("Error can only call after super.onAttach() is called")
    }

    protected fun addToDisposable(vararg disposables: Disposable) {
        compositeDisposable?.addAll(*disposables)
                ?: throw IllegalStateException("Error can only call after super.onAttach() is called")
    }

    override fun onDestroy() {
        super.onDestroy()
        store.clear()
    }

    fun viewModelProvider(factory: ViewModelProvider.Factory = defaultFactory(this)) = ViewModelProvider(store, factory)

    override fun getViewModelStore(): ViewModelStore = store

    companion object {
        private var defaultFactory: ViewModelProvider.Factory? = null

        private fun defaultFactory(controller: Controller): ViewModelProvider.Factory {
            val application = controller.activity?.application
                    ?: throw IllegalStateException("Your controller is not yet attached to Application.")
            return defaultFactory
                    ?: ViewModelProvider.AndroidViewModelFactory.getInstance(application).apply {
                        defaultFactory = this
                    }
        }

    }
}