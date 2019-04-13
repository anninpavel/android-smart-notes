package smartnotes.domain.usecase

import androidx.documentfile.provider.DocumentFile
import smartnotes.data.cache.PreferenceSource
import smartnotes.data.files.FileExplorer
import smartnotes.domain.values.ViewType
import smartnotes.presentation.usecase.UserUseCase
import smartnotes.utils.extensions.documentFile

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

    private companion object {
        private const val EXPORT_DIRECTORY_NAME = "SmartNotesExport"
    }
}
