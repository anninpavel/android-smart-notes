@file:JvmMultifileClass

package smartnotes.data.converters

import smartnotes.data.local.entities.NoteEntity
import smartnotes.data.local.views.NoteWithPhotosView
import smartnotes.domain.models.Note
import smartnotes.domain.values.NotePriority
import java.util.*

/**
 * Конвертер типов для заметок.
 *
 * @author Pavel Annin (https://github.com/anninpavel).
 */
class NoteConverter(private val photoConverter: PhotoConverter) {

    /** Возвращает новых экземпляр [NoteEntity], сформированный из [value]. */
    fun convert(value: Note): NoteEntity {
        return NoteEntity(
            id = value.id.value.toString(),
            title = value.title,
            text = value.text,
            priority = value.priority.string(),
            created = value.created
        )
    }

    /** Возвращает новых экземпляр [Note], сформированный из [value]. */
    fun convert(value: NoteEntity): Note {
        return Note(
            _id = UUID.fromString(value.id),
            title = value.title,
            text = value.text,
            priority = value.priority.notePriority(),
            created = value.created,
            photos = emptyList()
        )
    }

    /** Возвращает новых экземпляр [Note], сформированный из [value]. */
    fun convert(value: NoteWithPhotosView): Note {
        return convert(value.value).copy(
            photos = value.photos.map { photo -> photoConverter.convert(photo) }
        )
    }

    /** Возвращает строковое представление приоритета. */
    private fun NotePriority.string(): String {
        return name
    }

    /** Возвращает [NotePriority] из строкового представления. */
    private fun String.notePriority(): NotePriority {
        return NotePriority.valueOf(this)
    }
}
