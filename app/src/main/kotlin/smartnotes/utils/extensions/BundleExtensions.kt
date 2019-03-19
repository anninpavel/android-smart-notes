package smartnotes.utils.extensions

import android.app.Activity
import android.os.Bundle

/**
 * Возвращает объект [T] с заданным ключом [key].
 *
 * @throws IllegalStateException Если в данном [Bundle] отсутствует ключ [key].
 *
 * @see Bundle.get
 *
 * @author Pavel Annin (https://github.com/anninpavel).
 */
inline fun <reified T : Any> Bundle.require(key: String): T {
    check(value = containsKey(key), lazyMessage = { "Value with key $key not found." })
    return requireNotNull(get(key) as? T)
}

/**
 * Возвращает объект [T] с заданным ключом [key] из [android.content.Intent].
 *
 * @throws IllegalStateException Если в данном [Bundle] отсутствует ключ [key].
 *
 * @see Bundle.get
 *
 * @author Pavel Annin (https://github.com/anninpavel).
 */
inline fun <reified T : Any> Activity.requireExtra(key: String): T {
    return requireNotNull(intent?.extras).require(key)
}
