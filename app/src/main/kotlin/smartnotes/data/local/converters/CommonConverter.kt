package smartnotes.data.local.converters

import androidx.room.TypeConverter
import java.util.*

/**
 * Конвертер общих типов для базы данных.
 *
 * @author Pavel Annin (https://github.com/anninpavel).
 */
class CommonConverter {

    /** Конвертирует `timestamp` в экземпляр [Date]. */
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    /** Конвертирует экземпляр [Date] в `timestamp`. */
    @TypeConverter
    fun dateToTimestamp(value: Date?): Long? {
        return value?.time
    }
}
