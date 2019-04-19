package smartnotes.data.local.views

import androidx.room.Embedded
import androidx.room.Relation
import smartnotes.data.local.entities.NoteEntity
import smartnotes.data.local.entities.PhotoEntity

/**
 * Представление данных "Заметка сс снимками".
 *
 * @property value Экземпляр заметки.
 * @property photos Коллекция снимков.
 *
 * @author Pavel Annin (https://github.com/anninpavel).
 */
data class NoteWithPhotosView(
    @Embedded val value: NoteEntity,
    @Relation(parentColumn = "id", entityColumn = "note_id") val photos: List<PhotoEntity>
)
