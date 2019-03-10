@file:JvmMultifileClass

package smartnotes.utils.extensions

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders

/**
 * Возвращает экземпляр [ViewModel] или создает новых [ViewModel], для конкретного экземпляра [FragmentActivity].
 *
 * @param factory Фабрика для создания новых экземпляров [ViewModel].
 *
 * @receiver [FragmentActivity]
 *
 * @author Pavel Annin (https://github.com/anninpavel).
 */
inline fun <reified T : ViewModel> FragmentActivity.injectViewModel(factory: ViewModelProvider.Factory): T {
    return ViewModelProviders.of(this, factory)[T::class.java]
}

/**
 * Возвращает экземпляр [ViewModel] или создает новых [ViewModel], для конкретного экземпляра [Fragment].
 *
 * @param factory Фабрика для создания новых экземпляров [ViewModel].
 *
 * @receiver [Fragment]
 *
 * @author Pavel Annin (https://github.com/anninpavel).
 */
inline fun <reified T : ViewModel> Fragment.injectViewModel(factory: ViewModelProvider.Factory): T {
    return ViewModelProviders.of(this, factory)[T::class.java]
}
