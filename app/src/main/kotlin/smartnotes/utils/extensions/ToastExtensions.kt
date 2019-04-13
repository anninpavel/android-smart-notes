@file:JvmMultifileClass

package smartnotes.utils.extensions

import android.content.Context
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment

/**
 * Отображает стандартный короткий [Toast] с текстом [resId].
 *
 * @receiver [Context].
 *
 * @author Pavel Annin (https://github.com/anninpavel).
 */
fun Context.toast(@StringRes resId: Int) {
    Toast.makeText(this, resId, Toast.LENGTH_SHORT).show()
}

/**
 * Отображает стандартный короткий [Toast] с текстом [text].
 *
 * @receiver [Context].
 *
 * @author Pavel Annin (https://github.com/anninpavel).
 */
fun Context.toast(text: CharSequence) {
    Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
}

/**
 * Отображает стандартный длинный [Toast] с текстом [resId].
 *
 * @receiver [Context].
 *
 * @author Pavel Annin (https://github.com/anninpavel).
 */
fun Context.longToast(@StringRes resId: Int) {
    Toast.makeText(this, resId, Toast.LENGTH_LONG).show()
}

/**
 * Отображает стандартный длинный [Toast] с текстом [text].
 *
 * @receiver [Context].
 *
 * @author Pavel Annin (https://github.com/anninpavel).
 */
fun Context.longToast(text: CharSequence) {
    Toast.makeText(this, text, Toast.LENGTH_LONG).show()
}

/**
 * Отображает стандартный короткий [Toast] с текстом [resId].
 *
 * @receiver [Fragment].
 *
 * @author Pavel Annin (https://github.com/anninpavel).
 */
fun Fragment.toast(@StringRes resId: Int) {
    requireContext().toast(resId)
}

/**
 * Отображает стандартный короткий [Toast] с текстом [text].
 *
 * @receiver [Fragment].
 *
 * @author Pavel Annin (https://github.com/anninpavel).
 */
fun Fragment.toast(text: CharSequence) {
    requireContext().toast(text)
}

/**
 * Отображает стандартный длинный [Toast] с текстом [resId].
 *
 * @receiver [Fragment].
 *
 * @author Pavel Annin (https://github.com/anninpavel).
 */
fun Fragment.longToast(@StringRes resId: Int) {
    requireContext().longToast(resId)
}

/**
 * Отображает стандартный длинный [Toast] с текстом [text].
 *
 * @receiver [Fragment].
 *
 * @author Pavel Annin (https://github.com/anninpavel).
 */
fun Fragment.longToast(text: CharSequence) {
    requireContext().longToast(text)
}
