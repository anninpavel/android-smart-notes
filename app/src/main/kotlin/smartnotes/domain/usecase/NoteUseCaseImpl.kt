package smartnotes.domain.usecase

import androidx.documentfile.provider.DocumentFile
import androidx.lifecycle.LiveData
import io.reactivex.Completable
import smartnotes.data.files.FileExplorer
import smartnotes.domain.models.Note
import smartnotes.domain.repository.NoteRepository
import smartnotes.presentation.usecase.NoteUseCase
import smartnotes.utils.rx.SchedulerFacade

/**
 * Реализация [NoteUseCase].
 *
 * @property notes Репозиторий взаимодействия с заметками.
 * @property schedulers Планировщик.
 *
 * @author Pavel Annin (https://github.com/anninpavel).
 */
class NoteUseCaseImpl(
    private val notes: NoteRepository,
    private val files: FileExplorer,
    private val schedulers: SchedulerFacade
) : NoteUseCase {

    override fun liveFetchAll(): LiveData<List<Note>> {
        return notes.liveFetchAll()
    }

    override fun create(value: Note): Completable {
        return notes.create(value)
            .subscribeOn(schedulers.io)
            .observeOn(schedulers.ui)
    }

    override fun edit(value: Note): Completable {
        return notes.edit(value)
            .subscribeOn(schedulers.io)
            .observeOn(schedulers.ui)
    }

    override fun delete(value: Note): Completable {
        return notes.delete(value)
            .subscribeOn(schedulers.io)
            .observeOn(schedulers.ui)
    }

    override fun delete(values: List<Note>): Completable {
        return notes.delete(values)
            .subscribeOn(schedulers.io)
            .observeOn(schedulers.ui)
    }

    override fun exportToFile(value: Note, outputDirectory: DocumentFile): Completable {
        return Completable.fromAction {
            val fileName = if (value.title.isBlank()) DEFAULT_EXPORT_FILE_NAME else value.title.trim()
            files.createFile(outputDirectory, fileName, FileExplorer.FileNameExtension.TEXT).apply {
                files.writeToFile(this, value.asFileContents())
            }
        }
            .subscribeOn(schedulers.io)
            .observeOn(schedulers.ui)
    }

    private fun Note.asFileContents(): String {
        return StringBuilder().apply {
            if (title.isNotBlank()) {
                appendln(title)
                appendln()
            }
            append(text)
        }.toString()
    }

    private companion object {
        private const val DEFAULT_EXPORT_FILE_NAME = "no name"
    }
}
