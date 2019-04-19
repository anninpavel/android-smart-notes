package smartnotes.presentation.usecase

import androidx.documentfile.provider.DocumentFile
import androidx.lifecycle.LiveData
import io.reactivex.Completable
import smartnotes.domain.models.Note

/**
 * Интерфейс UseCase взаимодействия с "Заметками" из пользовательского представления.
 *
 * @author Pavel Annin (https://github.com/anninpavel).
 */
interface NoteUseCase {

    /**
     * Запрашивает все заметки.
     *
     * @return Коллекцию заметок обернутых в держатель данных [LiveData].
     */
    fun liveFetchAll(): LiveData<List<Note>>

    /**
     * Сохраняет заметку.
     *
     * @param value Сохраняемая заметка.
     */
    fun save(value: Note): Completable

    /**
     * Удаляет заметку.
     *
     * @param value Удаляемая заметка.
     */
    fun delete(value: Note): Completable

    /**
     * Удаляет коллекцию заметок.
     *
     * @param values Коллекция удаляемых заметок.
     */
    fun delete(values: List<Note>): Completable

    /**
     * Экспортирует заметку в текстовый файл.
     *
     * @param value Экспортируемая заметка.
     * @param outputDirectory Директория в которую будет сохранен экспортируемый файл.
     */
    fun exportToFile(value: Note, outputDirectory: DocumentFile): Completable
}
