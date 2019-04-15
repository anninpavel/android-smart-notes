@file:JvmMultifileClass

package smartnotes.utils.extensions

import android.content.res.Resources

/**
 * Конвертирует значение из px в dip.
 *
 * @author Pavel Annin (https://github.com/anninpavel).
 */
inline val Int.dp: Int
    get() = (this / Resources.getSystem().displayMetrics.density).toInt()

/**
 * Конвертирует значение из dip в px.
 *
 * @author Pavel Annin (https://github.com/anninpavel).
 */
inline val Int.px: Int
    get() = (this * Resources.getSystem().displayMetrics.density).toInt()
