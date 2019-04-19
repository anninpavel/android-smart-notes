package smartnotes.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import smartnotes.data.local.entities.NoteEntity

/**
 * Интерфейс предоставляющий доступ к данным "Заметок".
 *
 * @author Pavel Annin (https://github.com/anninpavel).
 */
@Dao
interface NoteDao : BaseDao<NoteEntity> {

    /**
     * Возвращает экземпляр зметки по идентификатору, если заметка отсутствует то `null`.
     *
     * @param value Идентификатор заметки.
     */
    @Query(value = "SELECT * from notes WHERE id == :value")
    fun byId(value: String): NoteEntity?
}
