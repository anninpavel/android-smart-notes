package smartnotes.presentation.screens.note

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import smartnotes.domain.models.Note
import smartnotes.presentation.common.Response
import smartnotes.presentation.usecase.NoteUseCase
import java.util.*
import javax.inject.Inject

/**
 * ViewModel экрана создания и редактирования заметки.
 *
 * @property disposables Контейнер подписок.
 * @property _liveCreateOrUpdateNote Содержит результат операции создание/обновления заметки.
 *
 * @property liveCreateOrUpdateNote Предоставляет публичный интерфейс [_liveCreateOrUpdateNote].
 *
 * @author Pavel Annin (https://github.com/anninpavel).
 */
class NoteDetailViewModel @Inject constructor(
    private val notes: NoteUseCase
) : ViewModel() {

    private val disposables = CompositeDisposable()
    private var _liveCreateOrUpdateNote = MutableLiveData<Response<Unit>>()

    val liveCreateOrUpdateNote: LiveData<Response<Unit>>
        get() = _liveCreateOrUpdateNote

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
            )
            .addTo(disposables)
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
            )
            .addTo(disposables)
    }
}
