@file:Suppress("UndocumentedPublicFunction")

package smartnotes.di.modules

import dagger.Module
import dagger.Provides
import smartnotes.data.converters.NoteConverter
import smartnotes.data.converters.PhotoConverter

/**
 * Модуль конверторов типов.
 *
 * @author Pavel Annin (https://github.com/anninpavel).
 */
@Module
class ConverterModule {

    @Provides
    fun provideNoteConverter(photos: PhotoConverter): NoteConverter {
        return NoteConverter(photos)
    }

    @Provides
    fun providePhotoConverter(): PhotoConverter {
        return PhotoConverter()
    }
}
