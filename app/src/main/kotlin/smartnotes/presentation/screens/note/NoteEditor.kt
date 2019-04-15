@file:JvmMultifileClass

package smartnotes.presentation.screens.note

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import smartnotes.domain.models.Note
import smartnotes.domain.values.NotePriority
import smartnotes.utils.kotlin.Memento
import smartnotes.utils.kotlin.MementoOriginator
import smartnotes.utils.kotlin.MementoSnapshot
import kotlin.properties.Delegates

/**
 * Редактор заметки.
 *
 * @property note Текущее состояние заметки.
 * @property liveNote Текущее состояние заметки, обернуто в держатель данных [LiveData].
 *
 * @author Pavel Annin (https://github.com/anninpavel).
 */
interface NoteEditor {

    var note: Note
    val liveNote: LiveData<Note>

    /** Заменяет загаловок заметки. */
    fun title(value: String)

    /** Заменяет текст заметки. */
    fun text(value: String)

    /** Заменяет приоритет заметки. */
    fun priority(value: NotePriority)
}

/**
 * Реализация редактора заметки.
 *
 * @author Pavel Annin (https://github.com/anninpavel).
 */
class NoteEditorImpl : NoteEditor, MementoOriginator<Note> {

    private val _liveNote = MutableLiveData<Note>()

    override var note by Delegates.observable(Note(title = "", text = "")) { _, _, newValue ->
        _liveNote.value = newValue
    }

    override val liveNote: LiveData<Note>
        get() = _liveNote

    override fun title(value: String) {
        note = note.copy(title = value)
    }

    override fun text(value: String) {
        note = note.copy(text = value)
    }

    override fun priority(value: NotePriority) {
        note = note.copy(priority = value)
    }

    override fun save(): Memento<Note> {
        return MementoSnapshot(note)
    }

    override fun restore(state: Memento<Note>) {
        note = state.value
    }
}
