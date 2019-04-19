package smartnotes.presentation.screens.note

import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_note_detail.view.*
import ru.github.anninpavel.smartnotes.R
import smartnotes.domain.models.Note
import smartnotes.domain.models.Photo
import smartnotes.presentation.share.decorators.ItemOffsetDecoration
import smartnotes.utils.extensions.px
import smartnotes.utils.kotlin.Action
import smartnotes.utils.kotlin.Consumer

/**
 * Представление экрана "Создание и редактирование заметки".
 *
 * @property photoAdapter Адаптер списка снимков.
 * @property isAutomaticChanged Флаг определяющий не пользовательский ввод данных на представлении.
 * @property isLocked Флаг определяющий доступность взаимодействия с представлением.
 * @property title Заголовок заметки отображаемое на представлении.
 * @property text Текст заметки отображаемый на представлении.
 * @property isUndoVisibility Флаг определяющий отображение отмены действия на представлении.
 *
 * @property onBackClick Событие, выбрано возвращение на предыдущий экран.
 * @property onMenuClick Событие, выбрано меню заметки.
 * @property onUndoClick Событие, выбрано отмена последнего действия.
 * @property onPhotoClick Событие, выбран снимок.
 * @property onPhotoRemoveClick Событие, выбрано удаление снимка.
 * @property onTitleChange Событие, изменен заголовок заметки.
 * @property onTextChange Событие, изменен текст заметки.
 *
 * @author Pavel Annin (https://github.com/anninpavel).
 */
class NoteDetailViewHolder(private val rootViewGroup: ViewGroup) {

    private val photoAdapter = PhotoNoteDetailAdapter()
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
    var onPhotoClick: Consumer<Photo>? = null
    var onPhotoRemoveClick: Consumer<Photo>? = null
    var onTitleChange: Consumer<CharSequence?>? = null
    var onTextChange: Consumer<CharSequence?>? = null

    init {
        with(rootViewGroup.noteDetailBottomAppBar) {
            menu?.clear()
            inflateMenu(R.menu.note_detail_bottom)
        }

        with(rootViewGroup.noteDetailPhotosRecyclerView) {
            layoutManager = GridLayoutManager(context, PHOTOS_SPAN_COUNT, RecyclerView.VERTICAL, false)
            addItemDecoration(ItemOffsetDecoration(offset = PHOTOS_ITEM_OFFSET.px))
            adapter = photoAdapter
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

        with(photoAdapter) {
            onItemClick = { onPhotoClick?.invoke(it) }
            onItemRemoveClick = { onPhotoRemoveClick?.invoke(it) }
        }
    }

    /** Этот метод вызывается для обновления контента представления. */
    fun onBind(data: Note?) {
        automaticChange {
            title = data?.title
            text = data?.text
            photoAdapter.submitList(data?.photos)
        }
    }

    private fun automaticChange(block: NoteDetailViewHolder.() -> Unit) {
        isAutomaticChanged = true
        apply(block)
        isAutomaticChanged = false
    }

    private companion object {
        private const val PHOTOS_SPAN_COUNT = 3
        private const val PHOTOS_ITEM_OFFSET = 8
    }
}
