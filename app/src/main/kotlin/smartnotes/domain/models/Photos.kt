@file:JvmMultifileClass

package smartnotes.domain.models

import android.net.Uri
import android.os.Parcelable
import kotlinx.android.parcel.IgnoredOnParcel
import kotlinx.android.parcel.Parcelize
import java.util.*

/**
 * Идентификатор снимка.
 *
 * @author Pavel Annin (https://github.com/anninpavel).
 */
inline class PhotoId(val value: UUID)

/**
 * Модель данных "Снимок".
 *
 * @property id Идентификатор заметки.
 * @property uri Путь к файлу снимка.
 *
 * @author Pavel Annin (https://github.com/anninpavel).
 */
@Parcelize
data class Photo(
    private val _id: UUID = UUID.randomUUID(),
    private val path: String
) : Parcelable {

    @IgnoredOnParcel
    val id: NoteId = NoteId(value = _id)

    @IgnoredOnParcel
    val uri: Uri = Uri.parse(path)
}
