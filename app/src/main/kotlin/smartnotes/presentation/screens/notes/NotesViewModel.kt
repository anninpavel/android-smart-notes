package smartnotes.presentation.screens.notes

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import smartnotes.domain.models.Note
import smartnotes.presentation.common.Response
import smartnotes.presentation.usecase.NoteUseCase
import javax.inject.Inject

/**
 * ViewModel экрана "Список заметок".
 *
 * @property liveNotes Коллекцию всех заметок обернутых в держатель данных [LiveData].
 * @property _liveDeleteNotes Содержит результат операции удаления заметок, обернуто в держатель данных [LiveData].
 *
 * @property liveDeleteNotes Предоставляет публичный интерфейс [_liveDeleteNotes].
 *
 * @author Pavel Annin (https://github.com/anninpavel).
 */
class NotesViewModel @Inject constructor(
    private val notes: NoteUseCase
) : ViewModel() {

    private val disposables = CompositeDisposable()
    private val _liveDeleteNotes = MutableLiveData<Response<Unit>>()

    val liveNotes: LiveData<List<Note>> by lazy(mode = LazyThreadSafetyMode.NONE) { notes.liveFetchAll() }

    val liveDeleteNotes: LiveData<Response<Unit>>
        get() = _liveDeleteNotes

    override fun onCleared() {
        super.onCleared()
        disposables.dispose()
    }

    /**
     * Удаляет коллекию заметок.
     * Результат оперции передается [_liveDeleteNotes].
     *
     * @param values Коллекция экземпляров удаляемых заметок.
     */
    fun delete(values: List<Note>) {
        notes.delete(values)
            .doOnSubscribe { _liveDeleteNotes.value = Response.loading() }
            .subscribe(
                { _liveDeleteNotes.value = Response.success(value = Unit) },
                { _liveDeleteNotes.value = Response.failure(error = it) }
            )
            .addTo(disposables)
    }
}
