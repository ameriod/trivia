package me.ameriod.trivia.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelStoreOwner
import me.ameriod.trivia.ui.MvvmController
import org.koin.android.viewmodel.ViewModelParameters
import org.koin.android.viewmodel.ViewModelStoreOwnerDefinition
import org.koin.android.viewmodel.getViewModel
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.qualifier.Qualifier
import kotlin.reflect.KClass

/**
 * Lazy get a viewModel instance
 *
 * @param qualifier - Koin BeanDefinition qualifier (if have several ViewModel beanDefinition of the same type)
 * @param parameters - parameters to pass to the BeanDefinition
 * @param clazz
 */
fun <T : ViewModel> MvvmController.viewModel(
        clazz: KClass<T>,
        qualifier: Qualifier? = null,
        parameters: ParametersDefinition? = null
): Lazy<T> = lazy { getViewModel(clazz, qualifier, parameters) }

/**
 * Lazy getByClass a viewModel instance
 *
 * @param qualifier - Koin BeanDefinition qualifier (if have several ViewModel beanDefinition of the same type)
 * @param parameters - parameters to pass to the BeanDefinition
 */
inline fun <reified T : ViewModel> MvvmController.viewModel(
        qualifier: Qualifier? = null,
        noinline parameters: ParametersDefinition? = null
): Lazy<T> = lazy { getViewModel<T>(qualifier, parameters) }

/**
 * Get a viewModel instance
 *
 * @param qualifier - Koin BeanDefinition qualifier (if have several ViewModel beanDefinition of the same type)
 * @param parameters - parameters to pass to the BeanDefinition
 */
inline fun <reified T : ViewModel> MvvmController.getViewModel(
        qualifier: Qualifier? = null,
        noinline parameters: ParametersDefinition? = null
): T {
    return getViewModel(T::class, qualifier, parameters)
}

/**
 * Lazy getByClass a viewModel instance
 *
 * @param clazz - Class of the BeanDefinition to retrieve
 * @param qualifier - Koin BeanDefinition qualifier (if have several ViewModel beanDefinition of the same type)
 * @param parameters - parameters to pass to the BeanDefinition
 */
fun <T : ViewModel> MvvmController.getViewModel(
        clazz: KClass<T>,
        qualifier: Qualifier? = null,
        parameters: ParametersDefinition? = null,
        from: ViewModelStoreOwnerDefinition = { this as ViewModelStoreOwner }
): T {
    return getKoin().getViewModel(
            ViewModelParameters(
                    clazz = clazz,
                    owner = this@getViewModel,
                    qualifier = qualifier,
                    from = from,
                    parameters = parameters
            )
    )
}

/**
 * Lazy getByClass a viewModel instance shared with Activity
 *
 * @param qualifier - Koin BeanDefinition qualifier (if have several ViewModel beanDefinition of the same type)
 * @param from - ViewModelStoreOwner that will store the viewModel instance. Examples: "parentFragment", "activity". Default: "activity"
 * @param parameters - parameters to pass to the BeanDefinition
 */
inline fun <reified T : ViewModel> MvvmController.sharedViewModel(
        qualifier: Qualifier? = null,
        noinline from: ViewModelStoreOwnerDefinition = { activity as ViewModelStoreOwner },
        noinline parameters: ParametersDefinition? = null
): Lazy<T> = lazy { getSharedViewModel<T>(qualifier, from, parameters) }

/**
 * Lazy getByClass a viewModel instance shared with Activity
 *
 * @param qualifier - Koin BeanDefinition qualifier (if have several ViewModel beanDefinition of the same type)
 * @param from - ViewModelStoreOwner that will store the viewModel instance. Examples: "parentMvvmController", "activity". Default: "activity"
 * @param parameters - parameters to pass to the BeanDefinition
 * @param clazz
 */
fun <T : ViewModel> MvvmController.sharedViewModel(
        clazz: KClass<T>,
        qualifier: Qualifier? = null,
        from: ViewModelStoreOwnerDefinition = { activity as ViewModelStoreOwner },
        parameters: ParametersDefinition? = null
): Lazy<T> = lazy { getSharedViewModel(clazz, qualifier, from, parameters) }

/**
 * Get a shared viewModel instance from underlying Activity
 *
 * @param qualifier - Koin BeanDefinition qualifier (if have several ViewModel beanDefinition of the same type)
 * @param from - ViewModelStoreOwner that will store the viewModel instance. Examples: ("parentMvvmController", "activity"). Default: "activity"
 * @param parameters - parameters to pass to the BeanDefinition
 */
inline fun <reified T : ViewModel> MvvmController.getSharedViewModel(
        qualifier: Qualifier? = null,
        noinline from: ViewModelStoreOwnerDefinition = { activity as ViewModelStoreOwner },
        noinline parameters: ParametersDefinition? = null
): T {
    return getSharedViewModel(T::class, qualifier, from, parameters)
}

/**
 * Get a shared viewModel instance from underlying Activity
 *
 * @param qualifier - Koin BeanDefinition qualifier (if have several ViewModel beanDefinition of the same type)
 * @param from - ViewModelStoreOwner that will store the viewModel instance. Examples: ("parentMvvmController", "activity"). Default: "activity"
 * @param parameters - parameters to pass to the BeanDefinition
 * @param clazz
 */
fun <T : ViewModel> MvvmController.getSharedViewModel(
        clazz: KClass<T>,
        qualifier: Qualifier? = null,
        from: ViewModelStoreOwnerDefinition = { activity as ViewModelStoreOwner },
        parameters: ParametersDefinition? = null
): T {
    return getKoin().getViewModel(
            ViewModelParameters(
                    clazz,
                    this,
                    qualifier,
                    from,
                    parameters
            )
    )
}