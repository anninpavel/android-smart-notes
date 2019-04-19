package smartnotes.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import smartnotes.data.local.views.NoteWithPhotosView

/**
 * Интерфейс предоставляющий доступ к данным заметок со снимками.
 *
 * @author Pavel Annin (https://github.com/anninpavel).
 */
@Dao
interface NoteWithPhotosDao {

    /**
     * Запрашивает все данные. Данные отсортированны по убыванию даты создания.
     *
     * @return Коллекцию заметок обернутых в держатель данных [LiveData].
     */
    @Transaction
    @Query(value = "SELECT * from notes ORDER BY created DESC")
    fun liveAllOrderByCreatedDesc(): LiveData<List<NoteWithPhotosView>>
}
