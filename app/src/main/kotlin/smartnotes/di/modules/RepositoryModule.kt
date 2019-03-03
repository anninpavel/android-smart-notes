@file:Suppress("UndocumentedPublicFunction")

package smartnotes.di.modules

import dagger.Module
import dagger.Provides
import smartnotes.data.local.DatabaseSource
import smartnotes.data.mapper.NoteMapper
import smartnotes.data.repository.NoteRepositoryImpl
import smartnotes.domain.repository.NoteRepository

/**
 * Модуль репозиториев приложения.
 *
 * @author Pavel Annin (https://github.com/anninpavel).
 */
@Module
class RepositoryModule {

    @Provides
    fun provideNoteRepository(db: DatabaseSource): NoteRepository {
        return NoteRepositoryImpl(db, mapper = NoteMapper())
    }
}
