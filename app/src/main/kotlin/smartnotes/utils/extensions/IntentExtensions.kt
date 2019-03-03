@file:JvmMultifileClass

package smartnotes.utils.extensions

import android.content.Context
import android.content.Intent

/**
 * Возвращает новый экземпляр [Intent] для заданого типа [T].
 *
 * @author Pavel Annin (https://github.com/anninpavel).
 */
inline fun <reified T : Any> Context.intentFor(): Intent {
    return Intent(this, T::class.java)
}
