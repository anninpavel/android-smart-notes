package smartnotes.presentation.screens.note

import android.view.ViewGroup
import kotlinx.android.synthetic.main.activity_note_detail.view.*
import smartnotes.domain.models.Note
import smartnotes.utils.kotlin.Action

/**
 * Представление экрана "Создание и редактирование заметки".
 *
 * @property isLocked Флаг определяющий доступность взаимодействия с представлением.
 * @property title Заголовок заметки, отображаемое на представлении.
 * @property text Текст заметки, отображаемый на представлении.
 * @property onBackClick Событие, выбрано возвращение на предыдущий экран.
 *
 * @author Pavel Annin (https://github.com/anninpavel).
 */
class NoteDetailViewHolder(private val rootViewGroup: ViewGroup) {

    var isLocked: Boolean = false
        set(value) = with(rootViewGroup) {
            field = value
            noteDetailTitleTextInputEditText.isEnabled = !value
            noteDetailTextInputEditText.isEnabled = !value
            noteDetailToolbar.isEnabled = !value
        }

    var title: CharSequence?
        get() = rootViewGroup.noteDetailTitleTextInputEditText.text
        set(value) = with(rootViewGroup.noteDetailTitleTextInputEditText) {
            setText(value)
            setSelection(length())
        }

    var text: CharSequence?
        get() = rootViewGroup.noteDetailTextInputEditText.text
        set(value) = with(rootViewGroup.noteDetailTextInputEditText) {
            setText(value)
            setSelection(length())
        }

    var onBackClick: Action? = null

    init {
        with(rootViewGroup) {
            noteDetailToolbar.setNavigationOnClickListener { onBackClick?.invoke() }
        }
    }

    /** Этот метод вызывается для обновления контента представления. */
    fun onBind(data: Note?) {
        title = data?.title
        text = data?.text
    }
}
