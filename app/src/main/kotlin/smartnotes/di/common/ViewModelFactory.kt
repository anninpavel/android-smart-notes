@file:JvmMultifileClass

package smartnotes.di.common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.MapKey
import javax.inject.Inject
import javax.inject.Provider
import javax.inject.Singleton
import kotlin.reflect.KClass

/**
 * Аннотаций ключа [ViewModel] для графа зависимости.
 *
 * @param value Значение ключа.
 *
 * @author Pavel Annin (https://github.com/anninpavel).
 */
@Target(
    AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.PROPERTY_SETTER
)
@MapKey
annotation class ViewModelKey(val value: KClass<out ViewModel>)

/**
 * Фабрика создания [ViewModel].
 *
 * @author Pavel Annin (https://github.com/anninpavel).
 */
@Singleton
@Suppress(names = ["UNCHECKED_CAST"])
class ViewModelFactory @Inject constructor(
    private val creators: Map<Class<out ViewModel>, @JvmSuppressWildcards Provider<ViewModel>>
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return creators[modelClass]?.get() as? T
            ?: throw IllegalArgumentException("Unknown view model class $modelClass")
    }
}
