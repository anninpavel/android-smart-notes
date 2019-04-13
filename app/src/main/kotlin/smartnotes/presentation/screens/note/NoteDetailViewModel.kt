package smartnotes.presentation.screens.note

import androidx.documentfile.provider.DocumentFile
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import smartnotes.domain.models.Note
import smartnotes.presentation.common.Response
import smartnotes.presentation.usecase.NoteUseCase
import smartnotes.presentation.usecase.UserUseCase
import java.util.*
import javax.inject.Inject

/**
 * ViewModel экрана создания и редактирования заметки.
 *
 * @property disposables Контейнер подписок.
 * @property _liveCreateOrUpdateNote Содержит результат операции создание/обновления заметки.
 * @property _liveDeleteNote Содержит результат операции удаления заметки.
 * @property _liveExportToFile Содержит результат операции експорта заметки в файл.
 *
 * @property liveCreateOrUpdateNote Предоставляет публичный интерфейс [_liveCreateOrUpdateNote].
 * @property liveDeleteNote Предоставляет публичный интерфейс [_liveDeleteNote].
 * @property liveExportToFile Предоставляет публичный интерфейс [_liveExportToFile].
 *
 * @author Pavel Annin (https://github.com/anninpavel).
 */
class NoteDetailViewModel @Inject constructor(
    private val notes: NoteUseCase,
    private val userUseCase: UserUseCase
) : ViewModel() {

    private val disposables = CompositeDisposable()
    private val _liveCreateOrUpdateNote = MutableLiveData<Response<Unit>>()
    private val _liveDeleteNote = MutableLiveData<Response<Unit>>()
    private val _liveExportToFile = MutableLiveData<Response<Unit>>()

    val liveCreateOrUpdateNote: LiveData<Response<Unit>>
        get() = _liveCreateOrUpdateNote

    val liveDeleteNote: LiveData<Response<Unit>>
        get() = _liveDeleteNote

    val liveExportToFile: LiveData<Response<Unit>>
        get() = _liveExportToFile

    override fun onCleared() {
        super.onCleared()
        disposables.dispose()
    }

    /**
     * Создает новую заметку.
     * Результат оперции передается [_liveCreateOrUpdateNote].
     *
     * @param title Заголовк заметки.
     * @param text Текст заметки.
     */
    fun create(title: CharSequence?, text: CharSequence?) {
        val note = Note(
            _id = UUID.randomUUID(),
            title = title?.toString() ?: "",
            text = text?.toString() ?: "",
            created = Date()
        )

        notes.create(note)
            .doOnSubscribe { _liveCreateOrUpdateNote.value = Response.loading() }
            .subscribe(
                { _liveCreateOrUpdateNote.value = Response.success(value = Unit) },
                { _liveCreateOrUpdateNote.value = Response.failure(error = it) }
            ).addTo(disposables)
    }

    /**
     * Сохраняет изменения в заметке.
     * Результат оперции передается [_liveCreateOrUpdateNote].
     *
     * @param original Исходный экземпляр заметки.
     * @param title Новый заголовк заметки.
     * @param text Новый текст заметки.
     */
    fun save(original: Note, title: CharSequence?, text: CharSequence?) {
        val note = original.copy(
            title = title?.toString() ?: "",
            text = text?.toString() ?: ""
        )
        notes.edit(note)
            .doOnSubscribe { _liveCreateOrUpdateNote.value = Response.loading() }
            .subscribe(
                { _liveCreateOrUpdateNote.value = Response.success(value = Unit) },
                { _liveCreateOrUpdateNote.value = Response.failure(error = it) }
            ).addTo(disposables)
    }

    /**
     * Удаляет заметку.
     * Результат оперции передается [_liveDeleteNote].
     *
     * @param value Экземпляр удаляемой заметки.
     */
    fun delete(value: Note) {
        notes.delete(value)
            .doOnSubscribe { _liveDeleteNote.value = Response.loading() }
            .subscribe(
                { _liveDeleteNote.value = Response.success(value = Unit) },
                { _liveDeleteNote.value = Response.failure(error = it) }
            ).addTo(disposables)
    }

    /**
     * Экспортирует заметку в файл.
     * Результат оперции передается [_liveExportToFile].
     *
     * @param title Заголовк заметки.
     * @param text Текст заметки.
     * @param desiredDirectory Желаемая директория для экспорта (опционально).
     */
    fun exportToFile(title: CharSequence?, text: CharSequence?, desiredDirectory: DocumentFile? = null) {
        val note = Note(
            _id = UUID.randomUUID(),
            title = title?.toString() ?: "",
            text = text?.toString() ?: "",
            created = Date()
        )

        val outputDirectory = userUseCase.exportDirectory(desiredDirectory)
        notes.exportToFile(note, outputDirectory)
            .doOnSubscribe { _liveExportToFile.value = Response.loading() }
            .subscribe(
                { _liveExportToFile.value = Response.success(value = Unit) },
                { _liveExportToFile.value = Response.failure(error = it) }
            ).addTo(disposables)
    }
}
