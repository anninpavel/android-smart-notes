package smartnotes.presentation.screens.note

import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import kotlinx.android.synthetic.main.activity_note_detail.view.*
import ru.github.anninpavel.smartnotes.R
import smartnotes.domain.models.Note
import smartnotes.utils.kotlin.Action
import smartnotes.utils.kotlin.Consumer

/**
 * Представление экрана "Создание и редактирование заметки".
 *
 * @property isAutomaticChanged Флаг определяющий не пользовательский ввод данных на представлении.
 * @property isLocked Флаг определяющий доступность взаимодействия с представлением.
 * @property title Заголовок заметки отображаемое на представлении.
 * @property text Текст заметки отображаемый на представлении.
 * @property isUndoVisibility Флаг определяющий отображение отмены действия на представлении.
 *
 * @property onBackClick Событие, выбрано возвращение на предыдущий экран.
 * @property onMenuClick Событие, выбрано меню заметки.
 * @property onUndoClick Событие, выбрано отмена последнего действия.
 * @property onTitleChange Событие, изменен заголовок заметки.
 * @property onTextChange Событие, изменен текст заметки.
 *
 * @author Pavel Annin (https://github.com/anninpavel).
 */
class NoteDetailViewHolder(private val rootViewGroup: ViewGroup) {

    private var isAutomaticChanged: Boolean = false

    var isLocked: Boolean = false
        set(value) = with(rootViewGroup) {
            field = value
            noteDetailTitleTextInputEditText.isEnabled = !value
            noteDetailTextInputEditText.isEnabled = !value
            noteDetailToolbar.isEnabled = !value
        }

    var title: CharSequence?
        get() = rootViewGroup.noteDetailTitleTextInputEditText.text
        private set(value) = with(rootViewGroup.noteDetailTitleTextInputEditText) {
            setText(value)
            setSelection(length())
        }

    var text: CharSequence?
        get() = rootViewGroup.noteDetailTextInputEditText.text
        private set(value) = with(rootViewGroup.noteDetailTextInputEditText) {
            setText(value)
            setSelection(length())
        }

    var isUndoVisibility: Boolean
        get() = rootViewGroup.noteDetailBottomAppBar.menu?.findItem(R.id.noteDetailUndoAction)?.isVisible ?: false
        set(value) {
            rootViewGroup.noteDetailBottomAppBar.menu?.findItem(R.id.noteDetailUndoAction)?.isVisible = value
        }

    var onBackClick: Action? = null
    var onMenuClick: Action? = null
    var onUndoClick: Action? = null
    var onTitleChange: Consumer<CharSequence?>? = null
    var onTextChange: Consumer<CharSequence?>? = null

    init {
        with(rootViewGroup.noteDetailBottomAppBar) {
            menu?.clear()
            inflateMenu(R.menu.note_detail_bottom)
        }

        with(rootViewGroup) {
            noteDetailToolbar.setNavigationOnClickListener { onBackClick?.invoke() }
            noteDetailTitleTextInputEditText.doOnTextChanged { text, _, _, _ ->
                if (!isAutomaticChanged) onTitleChange?.invoke(text)
            }
            noteDetailTextInputEditText.doOnTextChanged { text, _, _, _ ->
                if (!isAutomaticChanged) onTextChange?.invoke(text)
            }
            noteDetailBottomAppBar.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.noteDetailMoreAction -> onMenuClick?.invoke()
                    R.id.noteDetailUndoAction -> onUndoClick?.invoke()
                    else -> return@setOnMenuItemClickListener false
                }
                return@setOnMenuItemClickListener true
            }
        }
    }

    /** Этот метод вызывается для обновления контента представления. */
    fun onBind(data: Note?) {
        automaticChange {
            title = data?.title
            text = data?.text
        }
    }

    private fun automaticChange(block: NoteDetailViewHolder.() -> Unit) {
        isAutomaticChanged = true
        apply(block)
        isAutomaticChanged = false
    }
}
