package smartnotes.domain.usecase

import android.net.Uri
import androidx.documentfile.provider.DocumentFile
import smartnotes.data.cache.PreferenceSource
import smartnotes.data.files.FileExplorer
import smartnotes.domain.values.ViewType
import smartnotes.presentation.usecase.UserUseCase
import smartnotes.utils.extensions.documentFile
import java.util.*

/**
 * Реализация [UserUseCase].
 *
 * @property preferences Источник хранимых настроек приложения.
 * @property files Фаловый проводник.
 *
 * @author Pavel Annin (https://github.com/anninpavel).
 */
class UserUseCaseImpl(
    private val preferences: PreferenceSource,
    private val files: FileExplorer
) : UserUseCase {

    override fun fetchViewType(): ViewType {
        return preferences.userViewType
    }

    override fun saveViewType(value: ViewType) {
        preferences.userViewType = value
    }

    override fun exportDirectory(desiredDirectory: DocumentFile?): DocumentFile {
        val parentDirectory = desiredDirectory ?: files.documentDirectory().documentFile()
        return files.createDirectory(parentDirectory, EXPORT_DIRECTORY_NAME)
    }

    override fun createFileForPhoto(): Uri {
        val parentDirectory =  files.filesDirectory()
        val photosDirectory = files.createDirectory(parentDirectory, PHOTOS_DIRECTORY_NAME)
        val fileName = UUID.randomUUID().toString()
        val file = files.createFile(photosDirectory, fileName, FileExplorer.FileNameExtension.IMAGE_PNG)
        return files.grantFilePermission(file)
    }

    private companion object {
        private const val EXPORT_DIRECTORY_NAME = "SmartNotesExport"
        private const val PHOTOS_DIRECTORY_NAME = "photos"
    }
}
