package smartnotes.domain.repository

import androidx.lifecycle.LiveData
import io.reactivex.Completable
import smartnotes.domain.models.Note

/**
 * Интерфейс репоизитория данных "Заметок".
 *
 * @author Pavel Annin (https://github.com/anninpavel).
 */
interface NoteRepository {

    /**
     * Запрашивает все "Заметки".
     *
     * @return Коллекцию заметок обернутых в держатель данных [LiveData].
     */
    fun liveFetchAll(): LiveData<List<Note>>

    /**
     * Создает новую "Заметку".
     * Операция должна выполняться в побочном потоке.
     *
     * @param value Новая заметка.
     */
    fun create(value: Note): Completable

    /**
     * Редактирует "Заметку".
     * Операция должна выполняться в побочном потоке.
     *
     * @param value Редактируемая заметка.
     */
    fun edit(value: Note): Completable

    /**
     * Удаляет "Заметку".
     * Операция должна выполняться в побочном потоке.
     *
     * @param value Удаляемая заметка.
     */
    fun delete(value: Note): Completable

    /**
     * Удаляет коллекцию "Заметок".
     * Операция должна выполняться в побочном потоке.
     *
     * @param values Коллекция удаляемых заметок.
     */
    fun delete(values: List<Note>): Completable
}
