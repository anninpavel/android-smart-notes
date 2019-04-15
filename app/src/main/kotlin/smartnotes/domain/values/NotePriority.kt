@file:JvmMultifileClass

package smartnotes.domain.values

import android.content.Context
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import ru.github.anninpavel.smartnotes.R
import smartnotes.domain.values.NotePriority.HIGH
import smartnotes.domain.values.NotePriority.LOW
import smartnotes.domain.values.NotePriority.NORMAL
import smartnotes.domain.values.NotePriority.NO_PRIORITY

/**
 * Приоритетность заметки.
 *
 * - [NO_PRIORITY] Без приоритета.
 * - [LOW] Низкий приоритет.
 * - [NORMAL] Средний приоритет.
 * - [HIGH] Высоцкий приоритет.
 *
 * @author Pavel Annin (https://github.com/anninpavel).
 */
enum class NotePriority { NO_PRIORITY, LOW, NORMAL, HIGH }


/** Возвращает цвет приоритета. */
@ColorInt
fun NotePriority.color(context: Context): Int? {
    val resId = when (this) {
        NO_PRIORITY -> null
        LOW -> R.color.fruit_salad
        NORMAL -> R.color.amber
        HIGH -> R.color.pomegranate
    }
    return resId?.let { ContextCompat.getColor(context, resId) }
}
