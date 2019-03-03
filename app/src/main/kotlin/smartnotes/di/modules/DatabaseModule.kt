@file:Suppress("UndocumentedPublicFunction")

package smartnotes.di.modules

import android.content.Context
import dagger.Module
import dagger.Provides
import smartnotes.data.local.DatabaseSource
import javax.inject.Singleton

/**
 * Модуль репозиториев приложения.
 *
 * @author Pavel Annin (https://github.com/anninpavel).
 */
@Module
class DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabaseSource(context: Context): DatabaseSource {
        return DatabaseSource.create(context)
    }
}
