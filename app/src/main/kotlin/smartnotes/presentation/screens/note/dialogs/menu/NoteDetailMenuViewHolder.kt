package smartnotes.presentation.screens.note.dialogs.menu

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.dialog_note_detail_menu.view.*
import ru.github.anninpavel.smartnotes.R
import smartnotes.domain.values.NotePriority
import smartnotes.presentation.share.decorators.ItemOffsetDecoration
import smartnotes.utils.extensions.px
import smartnotes.utils.kotlin.Action
import smartnotes.utils.kotlin.Consumer

/**
 * Представление экрана "Меню заметки".
 *
 * @property onExportClick Событие, выбрано экспортирование заметки.
 * @property onRemoveClick Событие, выбрано удаление заметки.
 * @property onTakePhotoClick Событие, ывбрано сделать снимок.
 * @property onPriorityClick Событие, выбран приоритет заметки.
 *
 * @author Pavel Annin (https://github.com/anninpavel).
 */
class NoteDetailMenuViewHolder(rootView: View) {

    private val priorityAdapter = NoteDetailMenuPriorityAdapter()

    var onExportClick: Action? = null
    var onRemoveClick: Action? = null
    var onTakePhotoClick: Action? = null
    var onPriorityClick: Consumer<NotePriority>? = null

    init {
        with(rootView.dialogNoteDetailMenuPriorityRecyclerView) {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            addItemDecoration(ItemOffsetDecoration(offset = ITEM_OFFSET.px))
            adapter = priorityAdapter
        }

        priorityAdapter.onItemClick = { priority -> onPriorityClick?.invoke(priority) }
        rootView.dialogNoteDetailMenuNavigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.noteDetailExportAction -> onExportClick?.invoke()
                R.id.noteDetailRemoveAction -> onRemoveClick?.invoke()
                R.id.noteDetailTakePhotoAction -> onTakePhotoClick?.invoke()
                else -> return@setNavigationItemSelectedListener false
            }
            return@setNavigationItemSelectedListener true
        }
    }

    /** Этот метод вызывается для обновления контента представления. */
    fun onBind(priorities: List<NotePriority>, checked: NotePriority) {
        priorityAdapter.submitList(priorities, checked)
    }

    private companion object {
        private const val ITEM_OFFSET = 8
    }
}
