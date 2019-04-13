@file:JvmMultifileClass

package smartnotes.utils.extensions

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager

/**
 * Возвращает новый экземпляр [Intent] для заданого типа [T].
 *
 * @receiver [Context].
 *
 * @author Pavel Annin (https://github.com/anninpavel).
 */
inline fun <reified T : Any> Context.intentFor(): Intent {
    return Intent(this, T::class.java)
}

/**
 * Возвращает `true` если в системе есть хотя бы одно приложение который может обработать этот [Intent], иначе `false`.
 *
 * @param context Контекст приложения.
 *
 * @receiver [Intent].
 *
 * @author Pavel Annin (https://github.com/anninpavel).
 */
fun Intent.isSafe(context: Context): Boolean {
    return context.packageManager.queryIntentActivities(this, PackageManager.MATCH_DEFAULT_ONLY).size > 0
}
