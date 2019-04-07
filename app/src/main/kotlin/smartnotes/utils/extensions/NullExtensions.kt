@file:JvmMultifileClass

package smartnotes.utils.extensions

/**
 * Возвращает `true`, если [T] `null`, иначе `false`.
 *
 * @receiver [T].
 *
 * @author Pavel Annin (https://github.com/anninpavel).
 */
inline fun <reified T : Any> T?.isNull(): Boolean {
    return this == null
}
