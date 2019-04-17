package smartnotes.di.common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject
import javax.inject.Provider
import javax.inject.Singleton

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
