package smartnotes.data.files

import android.content.Context
import android.os.Environment
import androidx.documentfile.provider.DocumentFile
import java.io.File
import java.io.FileNotFoundException
import java.nio.charset.StandardCharsets

/**
 * Файловый проводник.
 *
 * @author Pavel Annin (https://github.com/anninpavel).
 */
class FileExplorer(private val context: Context) {

    /** Возвращает директорию "Документы". */
    fun documentDirectory(): File {
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).apply {
            if (!exists()) mkdirs()
        }
    }

    /** Создает директорию в каталоге [parent] с именем [name]. */
    fun createDirectory(parent: DocumentFile, name: String): DocumentFile {
        return parent.findFile(name)?.takeIf { it.exists() } ?: parent.createDirectory(name) ?: parent
    }

    /** Создает файл с именем [name] в директории [directory]. */
    fun createFile(directory: DocumentFile, name: String, type: FileNameExtension): DocumentFile {
        val fileName = directory.findFreeFileName(name, type)
        return directory.createFile(type.mime(), fileName)
            ?: throw FileNotFoundException("Failed to create file named $fileName")
    }

    /** Записывает содержимое [text] в файл [file]. */
    fun writeToFile(file: DocumentFile, text: String) {
        context.contentResolver.openOutputStream(file.uri)?.use {
            it.write(text.toByteArray(StandardCharsets.UTF_8))
        } ?: throw FileNotFoundException("File not found ${file.uri}")
    }

    /**
     * Возвращает совобоное имя файла [name]. Если имя файла занято то к файлу будет добавлен порядковый номер.
     *
     * @param name Имя файла.
     * @param type Расширение файла.
     * @param number Порядковый номер файла.
     *
     * @receiver [DocumentFile].
     */
    private tailrec fun DocumentFile.findFreeFileName(name: String, type: FileNameExtension, number: Int = 0): String {
        val fileName = when (number) {
            0 -> "$name.${type.extension()}"
            else -> "$name($number).${type.extension()}"
        }

        val freeFileName = fileName.takeUnless { findFile(it)?.exists() ?: false }
        return freeFileName ?: findFreeFileName(name, type, number + 1)
    }

    /** Возвращает строковое расширение файла. */
    private fun FileNameExtension.extension(): String {
        return when (this) {
            FileNameExtension.TEXT -> "txt"
        }
    }

    /** Возвращает спецификацию mime для расширения файла. */
    private fun FileNameExtension.mime(): String {
        return when (this) {
            FileNameExtension.TEXT -> "text/plain"
        }
    }

    /** Поддерживаемое расширение файла. */
    enum class FileNameExtension { TEXT }
}
