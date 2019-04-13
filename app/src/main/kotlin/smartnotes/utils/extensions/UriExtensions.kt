@file:JvmMultifileClass

package smartnotes.utils.extensions

import android.content.Context
import android.content.Intent
import android.net.Uri


/**
 * Устанавливает разрешение на постоянное использование [Uri].
 *
 * @param context Контекст приложения.
 *
 * @receiver [Uri].
 *
 * @see android.content.ContentResolver.takePersistableUriPermission
 *
 * @author Pavel Annin (https://github.com/anninpavel).
 */
fun Uri.takePersistablePermission(context: Context): Uri {
    return apply {
        context.contentResolver.takePersistableUriPermission(
            this,
            Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
        )
    }
}
