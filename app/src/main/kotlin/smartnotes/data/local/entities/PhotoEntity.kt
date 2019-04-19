package smartnotes.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

/**
 * Модель данных "Снимок".
 *
 * @property id Идентификатор снимка.
 * @property noteId Идентификатор заметки к кторой привязан снимок.
 * @property path Физический путь до снимка.
 *
 * @author Pavel Annin (https://github.com/anninpavel).
 */
@Entity(
    tableName = "photos",
    primaryKeys = ["id"],
    indices = [
        Index(value = ["note_id"])
    ],
    foreignKeys = [
        ForeignKey(
            entity = NoteEntity::class,
            parentColumns = ["id"],
            childColumns = ["note_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class PhotoEntity(
    @ColumnInfo(name = "id") val id: String,
    @ColumnInfo(name = "note_id") val noteId: String,
    @ColumnInfo(name = "path") val path: String
)
