package smartnotes.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import smartnotes.data.local.entities.PhotoEntity

/**
 * Интерфейс предоставляющий доступ к данным "Снимков заметок".
 *
 * @author Pavel Annin (https://github.com/anninpavel).
 */
@Dao
abstract class PhotoDao : BaseDao<PhotoEntity> {

    /** Удаляет записи по [noteId]. */
    @Query(value = "DELETE FROM photos WHERE note_id == :noteId")
    abstract fun deleteByNoteId(noteId: String)

    /** Заменяет все снимки связанные по `noteId`. */
    @Transaction
    open fun swap(elements: List<PhotoEntity>) {
        elements.firstOrNull()?.let { deleteByNoteId(noteId = it.noteId) }
        insertAll(elements)
    }
}
