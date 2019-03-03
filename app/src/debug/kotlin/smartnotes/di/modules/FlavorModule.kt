@file:Suppress("UndocumentedPublicFunction")

package smartnotes.di.modules

import dagger.Module
import dagger.Provides
import smartnotes.utils.log.DebugLogTree
import timber.log.Timber

/**
 * Конфигурируемый модуль приложения, модуль конфигурируется в зависимости от типа сборки.
 *
 * @author Pavel Annin (https://github.com/anninpavel).
 */
@Module
class FlavorModule {

    @Provides
    fun provideLoggerTree(): Timber.Tree {
        return DebugLogTree()
    }
}