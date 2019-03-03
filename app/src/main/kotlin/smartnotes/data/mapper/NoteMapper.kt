package smartnotes.data.mapper

import smartnotes.data.local.entities.NoteEntity
import smartnotes.domain.models.Note

/**
 * Конвертор типов [Note], [NoteEntity].
 *
 * @author Pavel Annin (https://github.com/anninpavel).
 */
class NoteMapper : BaseMapper<Note, NoteEntity> {

    override fun from(value: Note): NoteEntity {
        return NoteEntity(
            id = value.id.value,
            title = value.title,
            text = value.text,
            created = value.created
        )
    }

    override fun to(value: NoteEntity): Note {
        return Note(
            _id = value.id,
            title = value.title,
            text = value.text,
            created = value.created
        )
    }
}
