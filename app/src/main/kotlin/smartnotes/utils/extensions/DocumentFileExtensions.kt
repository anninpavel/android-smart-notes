@file:JvmMultifileClass

package smartnotes.utils.extensions

import android.content.Context
import android.net.Uri
import androidx.documentfile.provider.DocumentFile
import java.io.File

/**
 * Создание [DocumentFile] из [Uri].
 *
 * @param context Контекст приложения.
 *
 * @receiver [Uri].
 *
 * @author Pavel Annin (https://github.com/anninpavel).
 */
fun Uri.documentFile(context: Context): DocumentFile? {
    return DocumentFile.fromTreeUri(context, this)
}

/**
 * Создание [DocumentFile] из [File].
 *
 * @receiver [File].
 *
 * @author Pavel Annin (https://github.com/anninpavel).
 */
fun File.documentFile(): DocumentFile {
    return DocumentFile.fromFile(this)
}
