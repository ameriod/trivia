package me.ameriod.trivia.di

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import com.bluelinelabs.conductor.Controller
import com.bluelinelabs.conductor.archlifecycle.LifecycleController

/**
 * [LifecycleController] with a [ViewModelStore]
 */
abstract class MvvmController : LifecycleController, ViewModelStoreOwner {

    private val store by lazy(LazyThreadSafetyMode.NONE) { ViewModelStore() }

    constructor() : super()

    constructor(args: Bundle?) : super(args)


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