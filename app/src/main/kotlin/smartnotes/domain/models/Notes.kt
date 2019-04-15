@file:JvmMultifileClass

package smartnotes.domain.models

import android.os.Parcelable
import kotlinx.android.parcel.IgnoredOnParcel
import kotlinx.android.parcel.Parcelize
import smartnotes.domain.values.NotePriority
import java.util.*

/**
 * Идентификатор заметки.
 *
 * @author Pavel Annin (https://github.com/anninpavel).
 */
inline class NoteId(val value: UUID)

/**
 * Модель данных "Заметка".
 *
 * @property id Идентификатор заметки.
 * @property title Заголовок заметки.
 * @property text Текст заметки.
 * @property priority Приоритет заметки.
 * @property created Дата создания заметки.
 *
 * @author Pavel Annin (https://github.com/anninpavel).
 */
@Parcelize
data class Note(
    private val _id: UUID = UUID.randomUUID(),
    val title: String,
    val text: String,
    val priority: NotePriority = NotePriority.NO_PRIORITY,
    val created: Date = Date()
) : Parcelable {

    @IgnoredOnParcel
    val id: NoteId = NoteId(value = _id)
}
