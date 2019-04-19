@file:Suppress("UndocumentedPublicFunction")

package smartnotes.di.modules

import dagger.Module
import dagger.Provides
import smartnotes.data.converters.NoteConverter
import smartnotes.data.converters.PhotoConverter
import smartnotes.data.local.DatabaseSource
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
    fun provideNoteRepository(
        db: DatabaseSource,
        noteConverter: NoteConverter,
        photoConverter: PhotoConverter
    ): NoteRepository {
        return NoteRepositoryImpl(db, noteConverter, photoConverter)
    }
}
