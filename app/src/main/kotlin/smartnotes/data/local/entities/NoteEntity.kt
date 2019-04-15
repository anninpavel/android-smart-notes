package smartnotes.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import java.util.*

/**
 * Модель данных "Заметка".
 *
 * @property id Идентификатор заметки.
 * @property title Заголовок заметки.
 * @property text Текст заметки.
 * @property priority Приортитет заметки.
 * @property created Дата создания заметки.
 *
 * @author Pavel Annin (https://github.com/anninpavel).
 */
@Entity(
    tableName = "notes",
    primaryKeys = ["id"]
)
data class NoteEntity(
    @ColumnInfo(name = "id") val id: String,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "text") val text: String,
    @ColumnInfo(name = "priority") val priority: String,
    @ColumnInfo(name = "created") val created: Date
)
