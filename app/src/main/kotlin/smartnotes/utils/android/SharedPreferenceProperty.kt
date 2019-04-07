@file:JvmMultifileClass

package smartnotes.utils.android

import android.content.SharedPreferences
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * Делегат свойства, для [Boolean] значения.
 *
 * @param defaultValue Значение по умолчанию.
 * @param key Ключ значения, если ключ не задан будет подставлено имя параметра.
 *
 * @receiver [SharedPreferences].
 *
 * @author Pavel Annin (https://github.com/anninpavel).
 */
fun SharedPreferences.boolean(defaultValue: Boolean = false, key: String? = null): ReadWriteProperty<Any, Boolean> {
    return delegate(defaultValue, key, SharedPreferences::getBoolean, SharedPreferences.Editor::putBoolean)
}

/**
 * Делегат свойства, для [Int] значения.
 *
 * @param defaultValue Значение по умолчанию.
 * @param key Ключ значения, если ключ не задан будет подставлено имя параметра.
 *
 * @receiver [SharedPreferences].
 *
 * @author Pavel Annin (https://github.com/anninpavel).
 */
fun SharedPreferences.int(defaultValue: Int = 0, key: String? = null): ReadWriteProperty<Any, Int> {
    return delegate(defaultValue, key, SharedPreferences::getInt, SharedPreferences.Editor::putInt)
}

/**
 * Делегат свойства, для [Long] значения.
 *
 * @param defaultValue Значение по умолчанию.
 * @param key Ключ значения, если ключ не задан будет подставлено имя параметра.
 *
 * @receiver [SharedPreferences].
 *
 * @author Pavel Annin (https://github.com/anninpavel).
 */
fun SharedPreferences.long(defaultValue: Long = 0, key: String? = null): ReadWriteProperty<Any, Long> {
    return delegate(defaultValue, key, SharedPreferences::getLong, SharedPreferences.Editor::putLong)
}

/**
 * Делегат свойства, для [Float] значения.
 *
 * @param defaultValue Значение по умолчанию.
 * @param key Ключ значения, если ключ не задан будет подставлено имя параметра.
 *
 * @receiver [SharedPreferences].
 *
 * @author Pavel Annin (https://github.com/anninpavel).
 */
fun SharedPreferences.float(defaultValue: Boolean = false, key: String? = null): ReadWriteProperty<Any, Boolean> {
    return delegate(defaultValue, key, SharedPreferences::getBoolean, SharedPreferences.Editor::putBoolean)
}

/**
 * Делегат свойства, для [String] значения.
 *
 * @param defaultValue Значение по умолчанию.
 * @param key Ключ значения, если ключ не задан будет подставлено имя параметра.
 *
 * @receiver [SharedPreferences].
 *
 * @author Pavel Annin (https://github.com/anninpavel).
 */
fun SharedPreferences.string(defaultValue: String = "", key: String? = null): ReadWriteProperty<Any, String> {
    return delegate(defaultValue, key, SharedPreferences::getString, SharedPreferences.Editor::putString)
}

/**
 * Делегат свойства, для [String] значения, с поддержкой `null`.
 *
 * @param defaultValue Значение по умолчанию.
 * @param key Ключ значения, если ключ не задан будет подставлено имя параметра.
 *
 * @receiver [SharedPreferences].
 *
 * @author Pavel Annin (https://github.com/anninpavel).
 */
fun SharedPreferences.nullableString(
    defaultValue: String? = null,
    key: String? = null
): ReadWriteProperty<Any, String?> {
    return delegate(defaultValue, key, SharedPreferences::getString, SharedPreferences.Editor::putString)
}

/**
 * Делегат свойства, для [Set] значения.
 *
 * @param defaultValue Значение по умолчанию.
 * @param key Ключ значения, если ключ не задан будет подставлено имя параметра.
 *
 * @receiver [SharedPreferences].
 *
 * @author Pavel Annin (https://github.com/anninpavel).
 */
fun SharedPreferences.stringSet(
    defaultValue: Set<String> = emptySet(),
    key: String? = null
): ReadWriteProperty<Any, Set<String>> {
    return delegate(defaultValue, key, SharedPreferences::getStringSet, SharedPreferences.Editor::putStringSet)
}

/**
 * Делегат свойства, для [Set] значения, с поддержкой `null`.
 *
 * @param defaultValue Значение по умолчанию.
 * @param key Ключ значения, если ключ не задан будет подставлено имя параметра.
 *
 * @receiver [SharedPreferences].
 *
 * @author Pavel Annin (https://github.com/anninpavel).
 */
fun SharedPreferences.nullableStringSet(
    defaultValue: Set<String>? = null,
    key: String? = null
): ReadWriteProperty<Any, Set<String>?> {
    return delegate(defaultValue, key, SharedPreferences::getStringSet, SharedPreferences.Editor::putStringSet)
}

/**
 * Делегат свойства.
 *
 * @param defaultValue Значение по умолчанию.
 * @param key Ключ значения, если ключ не задан будет подставлено имя параметра.
 *
 * @receiver [SharedPreferences].
 *
 * @author Pavel Annin (https://github.com/anninpavel).
 */
private inline fun <T> SharedPreferences.delegate(
    defaultValue: T,
    key: String?,
    crossinline getter: SharedPreferences.(String, T) -> T,
    crossinline setter: SharedPreferences.Editor.(String, T) -> SharedPreferences.Editor
): ReadWriteProperty<Any, T> {
    return object : ReadWriteProperty<Any, T> {
        override fun getValue(thisRef: Any, property: KProperty<*>): T {
            return getter(key ?: property.name, defaultValue)
        }

        override fun setValue(thisRef: Any, property: KProperty<*>, value: T) {
            edit().setter(key ?: property.name, value).apply()
        }
    }
}
