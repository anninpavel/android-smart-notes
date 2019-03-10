package smartnotes.presentation.screens.notes

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import smartnotes.domain.models.Note
import smartnotes.presentation.usecase.NoteUseCase
import javax.inject.Inject

/**
 * ViewModel экрана "Список заметок".
 *
 * @property liveNotes Коллекцию всех заметок обернутых в держатель данных [LiveData].
 *
 * @author Pavel Annin (https://github.com/anninpavel).
 */
class NotesViewModel @Inject constructor(
    private val notes: NoteUseCase
) : ViewModel() {

    val liveNotes: LiveData<List<Note>> by lazy(mode = LazyThreadSafetyMode.NONE) { notes.liveFetchAll() }
}
