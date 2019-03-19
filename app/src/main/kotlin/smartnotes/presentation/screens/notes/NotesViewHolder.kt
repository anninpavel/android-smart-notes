package smartnotes.presentation.screens.notes

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import kotlinx.android.synthetic.main.activity_notes.view.*
import ru.github.anninpavel.smartnotes.R
import smartnotes.domain.models.Note
import smartnotes.presentation.common.viewholder.ViewHolder
import smartnotes.utils.kotlin.Action
import smartnotes.utils.kotlin.Consumer
import smartnotes.utils.kotlin.Supplier

/**
 * Представление экрана "Список заметок".
 *
 * @param viewTypeSupplier Поставщик типа отображения списка заметок.
 *
 * @property notesAdapter Адаптер представления списка заметок.
 * @property viewType Тип отображения списка заметок.
 *
 * @property onNoteClick Событие, выбрана заметка.
 * @property onCreateClick Событие, создание новой заметки.
 *
 * @author Pavel Annin (https://github.com/anninpavel).
 */
class NotesViewHolder(
    private val rootViewGroup: ViewGroup,
    viewTypeSupplier: Supplier<NotesAdapter.ViewType>
) : ViewHolder(rootViewGroup) {

    private val notesAdapter = NotesAdapter(viewType = NotesAdapter.ViewType.GRID)

    private var viewType: NotesAdapter.ViewType
        get() = notesAdapter.viewType
        set(value) {
            rootViewGroup.notesRecyclerView.layoutManager = value.layoutManager(rootViewGroup.context)
            notesAdapter.viewType = value
            with(rootViewGroup.notesBottomAppBar) {
                menu?.findItem(R.id.notesListAction)?.isVisible = value != NotesAdapter.ViewType.LIST
                menu?.findItem(R.id.notesGridAction)?.isVisible = value != NotesAdapter.ViewType.GRID
            }
        }

    var onNoteClick: Consumer<Note>? = null
    var onCreateClick: Action? = null

    init {
        with(rootViewGroup.notesBottomAppBar) {
            menu?.clear()
            inflateMenu(R.menu.notes_bottom)
        }

        viewType = viewTypeSupplier()
        rootViewGroup.notesRecyclerView.adapter = notesAdapter
        setEmptyContent(
            title = rootViewGroup.resources.getString(R.string.notes_label_empty_title),
            subTitle = rootViewGroup.resources.getString(R.string.notes_label_empty_subtitle)
        )

        notesAdapter.onItemClick = { note -> onNoteClick?.invoke(note) }
        rootViewGroup.notesCreateFloatingActionButton.setOnClickListener { onCreateClick?.invoke() }
        rootViewGroup.notesBottomAppBar.setOnMenuItemClickListener { menuItem ->
            viewType = when (menuItem.itemId) {
                R.id.notesListAction -> NotesAdapter.ViewType.LIST
                R.id.notesGridAction -> NotesAdapter.ViewType.GRID
                else -> return@setOnMenuItemClickListener false
            }
            return@setOnMenuItemClickListener true
        }
    }

    /** Этот метод вызывается для обновления контента представления. */
    fun onBind(data: List<Note>?) {
        notesAdapter.submitList(data)
        state = if (data.isNullOrEmpty()) State.Empty else State.Content
    }

    /**
     * Возвращает [RecyclerView.LayoutManager] в зависимости от [NotesAdapter.ViewType].
     *
     * @param context Контекст текущего представления.
     *
     * @receiver [NotesAdapter.ViewType]
     */
    private fun NotesAdapter.ViewType.layoutManager(context: Context): RecyclerView.LayoutManager {
        return when (this) {
            NotesAdapter.ViewType.LIST -> LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            NotesAdapter.ViewType.GRID -> StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        }
    }
}
