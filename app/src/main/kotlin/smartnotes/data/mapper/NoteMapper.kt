package smartnotes.data.mapper

import smartnotes.data.local.entities.NoteEntity
import smartnotes.domain.models.Note
import smartnotes.domain.values.NotePriority
import java.util.*

/**
 * Конвертор типов [Note], [NoteEntity].
 *
 * @author Pavel Annin (https://github.com/anninpavel).
 */
class NoteMapper : Mapper<Note, NoteEntity> {

    override fun from(value: Note): NoteEntity {
        return NoteEntity(
            id = value.id.value.toString(),
            title = value.title,
            text = value.text,
            priority = value.priority.string(),
            created = value.created
        )
    }

    override fun to(value: NoteEntity): Note {
        return Note(
            _id = UUID.fromString(value.id),
            title = value.title,
            text = value.text,
            priority = value.priority.notePriority(),
            created = value.created
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
