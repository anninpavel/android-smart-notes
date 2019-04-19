@file:JvmMultifileClass

package smartnotes.data.converters

import smartnotes.data.local.entities.PhotoEntity
import smartnotes.domain.models.NoteId
import smartnotes.domain.models.Photo
import java.util.*

/**
 * Конвертер типов для снимков.
 *
 * @author Pavel Annin (https://github.com/anninpavel).
 */
class PhotoConverter {

    /** Возвращает новых экземпляр [PhotoEntity], сформированный из [value]. */
    fun convert(value: Photo, noteId: NoteId): PhotoEntity {
        return PhotoEntity(
            id = value.id.value.toString(),
            noteId = noteId.value.toString(),
            path = value.uri.toString()
        )
    }

    /** Возвращает новых экземпляр [Photo], сформированный из [value]. */
    fun convert(value: PhotoEntity): Photo {
        return Photo(
            _id = UUID.fromString(value.id),
            path = value.path
        )
    }
}
