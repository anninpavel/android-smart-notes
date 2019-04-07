package smartnotes.presentation.usecase

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
     * Запрашивает все "Заметки".
     *
     * @return Коллекцию заметок обернутых в держатель данных [LiveData].
     */
    fun liveFetchAll(): LiveData<List<Note>>

    /**
     * Создает новую "Заметку".
     *
     * @param value Новая заметка.
     */
    fun create(value: Note): Completable

    /**
     * Редактирует "Заметку".
     *
     * @param value Редактируемая заметка.
     */
    fun edit(value: Note): Completable

    /**
     * Удаляет "Заметку".
     *
     * @param value Удаляемая заметка.
     */
    fun delete(value: Note): Completable

    /**
     * Удаляет коллекцию "Заметок".
     *
     * @param values Коллекция удаляемых заметок.
     */
    fun delete(values: List<Note>): Completable
}
