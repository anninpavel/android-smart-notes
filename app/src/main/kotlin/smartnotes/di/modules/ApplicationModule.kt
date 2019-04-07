@file:Suppress("UndocumentedPublicFunction")

package smartnotes.di.modules

import android.content.Context
import dagger.Module
import dagger.Provides
import smartnotes.SmartNotesApplication
import smartnotes.data.cache.PreferenceSource
import smartnotes.utils.rx.SchedulerFacade
import javax.inject.Singleton

/**
 * Основной модуль приложения.
 *
 * @author Pavel Annin (https://github.com/anninpavel).
 */
@Module(includes = [
    DatabaseModule::class,
    RepositoryModule::class,
    UseCaseModule::class,
    FlavorModule::class
])
class ApplicationModule {

    @Provides
    fun provideContext(application: SmartNotesApplication): Context {
        return application.applicationContext
    }

    @Provides
    @Singleton
    fun providePreferenceSource(context: Context): PreferenceSource {
        return PreferenceSource(context)
    }

    @Provides
    fun provideSchedulerFacade(): SchedulerFacade {
        return SchedulerFacade()
    }
}
