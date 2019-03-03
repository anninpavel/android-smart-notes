package smartnotes.data.local.dao

import androidx.lifecycle.LiveData
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
     * Запрашивает все данные.Данные отсортированны по убыванию даты создания.
     *
     * @return Коллекцию заметок обернутых в держатель данных [LiveData].
     */
    @Query(value = "SELECT * from notes ORDER BY created DESC")
    fun liveAllOrderByCreatedDesc(): LiveData<List<NoteEntity>>
}
